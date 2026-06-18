package afam.infrastructure.repository;

import afam.domain.entity.AccountStudente;
import afam.domain.entity.ContenutoMultimediale;
import afam.domain.entity.DatiCurriculari;
import afam.domain.entity.LinkDiCondivisione;
import afam.domain.repository.BoundaryDBMS;
import afam.util.SecurityUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class SQLiteBoundaryDBMS implements BoundaryDBMS {
    private final Path appDir = Paths.get(System.getProperty("user.home"), ".afam-identita-digitale");
    private final Path uploadDir = appDir.resolve("uploads");
    private final Path dbPath = appDir.resolve("afam_identita.sqlite");

    @Override
    public Path cartellaUpload() {
        return uploadDir;
    }

    @Override
    public void inizializza() throws Exception {
        Files.createDirectories(uploadDir);
        Class.forName("org.sqlite.JDBC");
        try (Connection c = connect(); Statement st = c.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
            st.execute("""
                    CREATE TABLE IF NOT EXISTS accounts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        email TEXT NOT NULL UNIQUE,
                        password_hash TEXT NOT NULL,
                        salt TEXT NOT NULL,
                        otp TEXT,
                        is_logged INTEGER NOT NULL DEFAULT 0,
                        recovery_token TEXT,
                        created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS curriculum (
                        student_id INTEGER PRIMARY KEY,
                        biografia TEXT DEFAULT '',
                        titoli_di_studio TEXT DEFAULT '',
                        esperienze_artistiche TEXT DEFAULT '',
                        updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(student_id) REFERENCES accounts(id) ON DELETE CASCADE
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS contents (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        student_id INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        category TEXT NOT NULL,
                        original_name TEXT NOT NULL,
                        format TEXT NOT NULL,
                        size_bytes INTEGER NOT NULL,
                        file_path TEXT NOT NULL,
                        position INTEGER NOT NULL,
                        created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(student_id) REFERENCES accounts(id) ON DELETE CASCADE
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS share_links (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        student_id INTEGER NOT NULL,
                        url TEXT NOT NULL UNIQUE,
                        description TEXT,
                        expires_on TEXT,
                        active INTEGER NOT NULL DEFAULT 1,
                        view_count INTEGER NOT NULL DEFAULT 0,
                        created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(student_id) REFERENCES accounts(id) ON DELETE CASCADE
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS share_link_contents (
                        link_id INTEGER NOT NULL,
                        content_id INTEGER NOT NULL,
                        PRIMARY KEY(link_id, content_id),
                        FOREIGN KEY(link_id) REFERENCES share_links(id) ON DELETE CASCADE,
                        FOREIGN KEY(content_id) REFERENCES contents(id) ON DELETE CASCADE
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS views (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        link_id INTEGER NOT NULL,
                        viewer_label TEXT NOT NULL,
                        viewed_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(link_id) REFERENCES share_links(id) ON DELETE CASCADE
                    )
                    """);
        }
    }

    private Connection connect() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toAbsolutePath());
        try (Statement st = c.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return c;
    }

    @Override
    public boolean emailEsiste(String email) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT 1 FROM accounts WHERE email=?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void salvaNuovoAccount(String email, String password) throws SQLException {
        String salt = SecurityUtil.newSalt();
        String hash = SecurityUtil.hashPassword(password, salt);
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO accounts(email,password_hash,salt) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ps.setString(2, hash);
            ps.setString(3, salt);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) salvaDatiCurriculari(keys.getInt(1), "", "", "");
            }
        }
    }

    @Override
    public Optional<AccountStudente> cercaAccountPerEmail(String email) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT id,email FROM accounts WHERE email=?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new AccountStudente(rs.getInt("id"), rs.getString("email")));
                return Optional.empty();
            }
        }
    }

    @Override
    public AccountStudente recuperaOCreaAccountProvider(String email) throws SQLException {
        Optional<AccountStudente> existing = cercaAccountPerEmail(email);
        if (existing.isPresent()) return existing.get();
        String salt = SecurityUtil.newSalt();
        String randomPasswordHash = SecurityUtil.hashPassword(UUID.randomUUID().toString(), salt);
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO accounts(email,password_hash,salt) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ps.setString(2, randomPasswordHash);
            ps.setString(3, salt);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                int id = keys.next() ? keys.getInt(1) : -1;
                salvaDatiCurriculari(id, "", "", "");
                return new AccountStudente(id, email);
            }
        }
    }

    @Override
    public Optional<AccountStudente> autentica(String email, String password) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT id,email,password_hash,salt FROM accounts WHERE email=?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                String hash = SecurityUtil.hashPassword(password, rs.getString("salt"));
                if (hash.equals(rs.getString("password_hash"))) {
                    return Optional.of(new AccountStudente(rs.getInt("id"), rs.getString("email")));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean verificaPassword(int accountStudenteId, String password) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT password_hash,salt FROM accounts WHERE id=?")) {
            ps.setInt(1, accountStudenteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                String hash = SecurityUtil.hashPassword(password, rs.getString("salt"));
                return hash.equals(rs.getString("password_hash"));
            }
        }
    }

    @Override
    public void aggiornaPassword(int accountStudenteId, String nuovaPassword) throws SQLException {
        String salt = SecurityUtil.newSalt();
        String hash = SecurityUtil.hashPassword(nuovaPassword, salt);
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "UPDATE accounts SET password_hash=?, salt=? WHERE id=?")) {
            ps.setString(1, hash);
            ps.setString(2, salt);
            ps.setInt(3, accountStudenteId);
            ps.executeUpdate();
        }
    }

    @Override
    public void aggiornaStatoLogin(int accountStudenteId, boolean logged) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE accounts SET is_logged=?, otp=NULL WHERE id=?")) {
            ps.setInt(1, logged ? 1 : 0);
            ps.setInt(2, accountStudenteId);
            ps.executeUpdate();
        }
    }

    @Override
    public void salvaOTP(int accountStudenteId, String otp) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE accounts SET otp=? WHERE id=?")) {
            ps.setString(1, otp);
            ps.setInt(2, accountStudenteId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean verificaOTP(int accountStudenteId, String otp) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT otp FROM accounts WHERE id=?")) {
            ps.setInt(1, accountStudenteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && otp != null && otp.equals(rs.getString("otp"));
            }
        }
    }

    @Override
    public void salvaTokenRipristino(int accountStudenteId, String token) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE accounts SET recovery_token=? WHERE id=?")) {
            ps.setString(1, token);
            ps.setInt(2, accountStudenteId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean verificaTokenRipristino(int accountStudenteId, String token) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT recovery_token FROM accounts WHERE id=?")) {
            ps.setInt(1, accountStudenteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && token != null && token.equals(rs.getString("recovery_token"));
            }
        }
    }

    @Override
    public void cancellaTokenRipristino(int accountStudenteId) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE accounts SET recovery_token=NULL WHERE id=?")) {
            ps.setInt(1, accountStudenteId);
            ps.executeUpdate();
        }
    }

    @Override
    public DatiCurriculari recuperaDatiCurriculari(int accountStudenteId) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT biografia,titoli_di_studio,esperienze_artistiche FROM curriculum WHERE student_id=?")) {
            ps.setInt(1, accountStudenteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new DatiCurriculari(rs.getString(1), rs.getString(2), rs.getString(3));
                return DatiCurriculari.vuoti();
            }
        }
    }

    @Override
    public void salvaDatiCurriculari(int accountStudenteId, String biografia, String titoliDiStudio, String esperienzeArtisticheEFormative) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("""
                INSERT INTO curriculum(student_id,biografia,titoli_di_studio,esperienze_artistiche,updated_at)
                VALUES(?,?,?,?,CURRENT_TIMESTAMP)
                ON CONFLICT(student_id) DO UPDATE SET
                    biografia=excluded.biografia,
                    titoli_di_studio=excluded.titoli_di_studio,
                    esperienze_artistiche=excluded.esperienze_artistiche,
                    updated_at=CURRENT_TIMESTAMP
                """)) {
            ps.setInt(1, accountStudenteId);
            ps.setString(2, truncate(biografia, 200));
            ps.setString(3, truncate(titoliDiStudio, 200));
            ps.setString(4, truncate(esperienzeArtisticheEFormative, 200));
            ps.executeUpdate();
        }
    }

    @Override
    public void salvaContenuto(int accountStudenteId, String titolo, String tipo, String nomeOriginale, String formato, long dimensione, Path percorsoFile) throws SQLException {
        int pos = nextPosition(accountStudenteId);
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("""
                INSERT INTO contents(student_id,title,category,original_name,format,size_bytes,file_path,position)
                VALUES(?,?,?,?,?,?,?,?)
                """)) {
            ps.setInt(1, accountStudenteId);
            ps.setString(2, titolo);
            ps.setString(3, tipo);
            ps.setString(4, nomeOriginale);
            ps.setString(5, formato);
            ps.setLong(6, dimensione);
            ps.setString(7, percorsoFile.toAbsolutePath().toString());
            ps.setInt(8, pos);
            ps.executeUpdate();
        }
    }

    private int nextPosition(int accountStudenteId) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT COALESCE(MAX(position),0)+1 FROM contents WHERE student_id=?")) {
            ps.setInt(1, accountStudenteId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 1; }
        }
    }

    @Override
    public List<ContenutoMultimediale> recuperaContenuti(int accountStudenteId) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM contents WHERE student_id=? ORDER BY position ASC, id ASC")) {
            ps.setInt(1, accountStudenteId);
            try (ResultSet rs = ps.executeQuery()) {
                List<ContenutoMultimediale> out = new ArrayList<>();
                while (rs.next()) out.add(contenutoFrom(rs));
                return out;
            }
        }
    }

    @Override
    public List<ContenutoMultimediale> recuperaContenutiPerLink(int linkId) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("""
                SELECT c.* FROM contents c
                JOIN share_link_contents slc ON slc.content_id=c.id
                WHERE slc.link_id=?
                ORDER BY c.position ASC, c.id ASC
                """)) {
            ps.setInt(1, linkId);
            try (ResultSet rs = ps.executeQuery()) {
                List<ContenutoMultimediale> out = new ArrayList<>();
                while (rs.next()) out.add(contenutoFrom(rs));
                return out;
            }
        }
    }

    private ContenutoMultimediale contenutoFrom(ResultSet rs) throws SQLException {
        return new ContenutoMultimediale(rs.getInt("id"), rs.getInt("student_id"), rs.getString("title"),
                rs.getString("category"), rs.getString("original_name"), rs.getString("format"),
                rs.getLong("size_bytes"), Paths.get(rs.getString("file_path")), rs.getInt("position"));
    }

    @Override
    public void eliminaContenuto(int contenutoId) throws SQLException, IOException {
        Path filePath = null;
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT file_path FROM contents WHERE id=?")) {
            ps.setInt(1, contenutoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) filePath = Paths.get(rs.getString(1));
            }
        }
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("DELETE FROM contents WHERE id=?")) {
            ps.setInt(1, contenutoId);
            ps.executeUpdate();
        }
        if (filePath != null) Files.deleteIfExists(filePath);
    }

    @Override
    public void salvaOrdineContenuti(List<ContenutoMultimediale> contenuti) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE contents SET position=? WHERE id=?")) {
            int pos = 1;
            for (ContenutoMultimediale item : contenuti) {
                ps.setInt(1, pos++);
                ps.setInt(2, item.id());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public LinkDiCondivisione salvaLinkDiCondivisione(int accountStudenteId, List<Integer> contenutiIds, String descrizione, LocalDate dataDiScadenza) throws SQLException {
        String url = "AFAM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement("""
                    INSERT INTO share_links(student_id,url,description,expires_on,active,view_count)
                    VALUES(?,?,?,?,1,0)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, accountStudenteId);
                ps.setString(2, url);
                ps.setString(3, descrizione == null ? "" : descrizione);
                ps.setString(4, dataDiScadenza == null ? null : dataDiScadenza.toString());
                ps.executeUpdate();
                int linkId;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Impossibile generare il link.");
                    linkId = keys.getInt(1);
                }
                try (PreparedStatement ps2 = c.prepareStatement("INSERT INTO share_link_contents(link_id,content_id) VALUES(?,?)")) {
                    for (int id : contenutiIds) {
                        ps2.setInt(1, linkId);
                        ps2.setInt(2, id);
                        ps2.addBatch();
                    }
                    ps2.executeBatch();
                }
                c.commit();
                return new LinkDiCondivisione(linkId, accountStudenteId, url, descrizione, dataDiScadenza, true, 0);
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    @Override
    public List<LinkDiCondivisione> recuperaLinkDiCondivisione(int accountStudenteId, boolean soloAttivi) throws SQLException {
        String sql = "SELECT * FROM share_links WHERE student_id=?" + (soloAttivi ? " AND active=1" : "") + " ORDER BY created_at DESC";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountStudenteId);
            try (ResultSet rs = ps.executeQuery()) {
                List<LinkDiCondivisione> out = new ArrayList<>();
                while (rs.next()) out.add(linkFrom(rs));
                return out;
            }
        }
    }

    @Override
    public Optional<LinkDiCondivisione> recuperaLinkValido(String url) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT * FROM share_links WHERE url=?")) {
            ps.setString(1, url);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                LinkDiCondivisione link = linkFrom(rs);
                if (!link.attivo()) return Optional.empty();
                if (link.dataDiScadenza() != null && link.dataDiScadenza().isBefore(LocalDate.now())) return Optional.empty();
                return Optional.of(link);
            }
        }
    }

    private LinkDiCondivisione linkFrom(ResultSet rs) throws SQLException {
        String date = rs.getString("expires_on");
        LocalDate expires = (date == null || date.isBlank()) ? null : LocalDate.parse(date);
        return new LinkDiCondivisione(rs.getInt("id"), rs.getInt("student_id"), rs.getString("url"),
                rs.getString("description"), expires, rs.getInt("active") == 1, rs.getInt("view_count"));
    }

    @Override
    public void registraVisualizzazione(int linkId, String etichettaVisualizzatore) throws SQLException {
        
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO views(link_id,viewer_label) VALUES(?,?)")) {
                    ps.setInt(1, linkId);
                    ps.setString(2, etichettaVisualizzatore);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE share_links SET view_count=view_count+1 WHERE id=?")) {
                    ps.setInt(1, linkId);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    @Override
    public List<String> recuperaRigheVisualizzazione(int linkId) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("""
                SELECT viewer_label, viewed_at FROM views
                WHERE link_id=? ORDER BY viewed_at DESC
                """)) {
            ps.setInt(1, linkId);
            try (ResultSet rs = ps.executeQuery()) {
                List<String> rows = new ArrayList<>();
                while (rs.next()) rows.add(rs.getString("viewer_label") + " | " + rs.getString("viewed_at"));
                return rows;
            }
        }
    }

    @Override
    public void disattivaLink(int linkId) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE share_links SET active=0 WHERE id=?")) {
            ps.setInt(1, linkId);
            ps.executeUpdate();
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max);
    }
}
