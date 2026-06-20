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
                    CREATE TABLE IF NOT EXISTS AccountStudente (
                        idAccount INTEGER PRIMARY KEY AUTOINCREMENT,
                        email TEXT NOT NULL UNIQUE,
                        pwd TEXT NOT NULL,
                        salt TEXT NOT NULL,
                        isLogged INTEGER NOT NULL DEFAULT 0,
                        recoveryToken TEXT,
                        createdAt TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS DatiCurriculari (
                        idDati INTEGER PRIMARY KEY AUTOINCREMENT,
                        idAccount INTEGER NOT NULL UNIQUE,
                        biografia TEXT DEFAULT '',
                        titoliDiStudio TEXT DEFAULT '',
                        esperienze TEXT DEFAULT '',
                        updatedAt TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(idAccount) REFERENCES AccountStudente(idAccount) ON DELETE CASCADE
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS ContenutoMultimediale (
                        idContenuto INTEGER PRIMARY KEY AUTOINCREMENT,
                        idAccount INTEGER NOT NULL,
                        titolo TEXT NOT NULL,
                        formato TEXT NOT NULL,
                        dimensione INTEGER NOT NULL,
                        filePath TEXT NOT NULL,
                        posizione INTEGER NOT NULL,
                        createdAt TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(idAccount) REFERENCES AccountStudente(idAccount) ON DELETE CASCADE
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS LinkCondivisione (
                        idLink INTEGER PRIMARY KEY AUTOINCREMENT,
                        idStudente INTEGER NOT NULL,
                        url TEXT NOT NULL UNIQUE,
                        descrizione TEXT,
                        dataDiScadenza TEXT,
                        numeroVisualizzazioni INTEGER NOT NULL DEFAULT 0,
                        createdAt TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(idStudente) REFERENCES AccountStudente(idAccount) ON DELETE CASCADE
                    )
                    """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS ContenutiVisibili (
                        linkId INTEGER NOT NULL,
                        contentId INTEGER NOT NULL,
                        PRIMARY KEY(linkId, contentId),
                        FOREIGN KEY(linkId) REFERENCES LinkCondivisione(idLink) ON DELETE CASCADE,
                        FOREIGN KEY(contentId) REFERENCES ContenutoMultimediale(idContenuto) ON DELETE CASCADE
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
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT 1 FROM AccountStudente WHERE email=?")) {
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
                "INSERT INTO AccountStudente(email,pwd,salt) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT idAccount,email FROM AccountStudente WHERE email=?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new AccountStudente(rs.getInt("idAccount"), rs.getString("email")));
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
                "INSERT INTO AccountStudente(email,pwd,salt) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ps.setString(2, randomPasswordHash);
            ps.setString(3, salt);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                int idAccount = keys.next() ? keys.getInt(1) : -1;
                salvaDatiCurriculari(idAccount, "", "", "");
                return new AccountStudente(idAccount, email);
            }
        }
    }

    @Override
    public Optional<AccountStudente> autentica(String email, String password) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT idAccount,email,pwd,salt FROM AccountStudente WHERE email=?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                String hash = SecurityUtil.hashPassword(password, rs.getString("salt"));
                if (hash.equals(rs.getString("pwd"))) {
                    return Optional.of(new AccountStudente(rs.getInt("idAccount"), rs.getString("email")));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean verificaPassword(int idAccount, String password) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT pwd,salt FROM AccountStudente WHERE idAccount=?")) {
            ps.setInt(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                String hash = SecurityUtil.hashPassword(password, rs.getString("salt"));
                return hash.equals(rs.getString("pwd"));
            }
        }
    }

    @Override
    public void aggiornaPassword(int idAccount, String nuovaPassword) throws SQLException {
        String salt = SecurityUtil.newSalt();
        String hash = SecurityUtil.hashPassword(nuovaPassword, salt);
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "UPDATE AccountStudente SET pwd=?, salt=? WHERE idAccount=?")) {
            ps.setString(1, hash);
            ps.setString(2, salt);
            ps.setInt(3, idAccount);
            ps.executeUpdate();
        }
    }

    @Override
    public void aggiornaStatoLogin(int idAccount, boolean logged) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE AccountStudente SET isLogged=? WHERE idAccount=?")) {
            ps.setInt(1, logged ? 1 : 0);
            ps.setInt(2, idAccount);
            ps.executeUpdate();
        }
    }

    @Override
    public void salvaTokenRipristino(int idAccount, String token) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE AccountStudente SET recoveryToken=? WHERE idAccount=?")) {
            ps.setString(1, token);
            ps.setInt(2, idAccount);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean verificaTokenRipristino(int idAccount, String token) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT recoveryToken FROM AccountStudente WHERE idAccount=?")) {
            ps.setInt(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && token != null && token.equals(rs.getString("recoveryToken"));
            }
        }
    }

    @Override
    public void cancellaTokenRipristino(int idAccount) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE AccountStudente SET recoveryToken=NULL WHERE idAccount=?")) {
            ps.setInt(1, idAccount);
            ps.executeUpdate();
        }
    }

    @Override
    public DatiCurriculari recuperaDatiCurriculari(int idAccount) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT biografia,titoliDiStudio,esperienze FROM DatiCurriculari WHERE idAccount=?")) {
            ps.setInt(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new DatiCurriculari(rs.getString(1), rs.getString(2), rs.getString(3));
                return DatiCurriculari.vuoti();
            }
        }
    }

    @Override
    public void salvaDatiCurriculari(int idAccount, String biografia, String titoliDiStudio, String esperienzeArtisticheEFormative) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("""
                INSERT INTO DatiCurriculari(idAccount,biografia,titoliDiStudio,esperienze,updatedAt)
                VALUES(?,?,?,?,CURRENT_TIMESTAMP)
                ON CONFLICT(idAccount) DO UPDATE SET
                    biografia=excluded.biografia,
                    titoliDiStudio=excluded.titoliDiStudio,
                    esperienze=excluded.esperienze,
                    updatedAt=CURRENT_TIMESTAMP
                """)) {
            ps.setInt(1, idAccount);
            ps.setString(2, truncate(biografia, 200));
            ps.setString(3, truncate(titoliDiStudio, 200));
            ps.setString(4, truncate(esperienzeArtisticheEFormative, 200));
            ps.executeUpdate();
        }
    }

    @Override
    public void salvaContenuto(int idAccount, String titolo, String formato, long dimensione, Path percorsoFile) throws SQLException {
        int pos = nextPosition(idAccount);
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("""
                INSERT INTO ContenutoMultimediale(idAccount,titolo,formato,dimensione,filePath,posizione)
                VALUES(?,?,?,?,?,?)
                """)) {
            ps.setInt(1, idAccount);
            ps.setString(2, titolo);
            ps.setString(3, formato);
            ps.setLong(4, dimensione);
            ps.setString(5, percorsoFile.toAbsolutePath().toString());
            ps.setInt(6, pos);
            ps.executeUpdate();
        }
    }

    private int nextPosition(int idAccount) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT COALESCE(MAX(posizione),0)+1 FROM ContenutoMultimediale WHERE idAccount=?")) {
            ps.setInt(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 1; }
        }
    }

    @Override
    public List<ContenutoMultimediale> recuperaContenuti(int idAccount) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM ContenutoMultimediale WHERE idAccount=? ORDER BY posizione ASC, idContenuto ASC")) {
            ps.setInt(1, idAccount);
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
                SELECT c.* FROM ContenutoMultimediale c
                JOIN ContenutiVisibili slc ON slc.contentId=c.idContenuto
                WHERE slc.linkId=?
                ORDER BY c.posizione ASC, c.idContenuto ASC
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
        return new ContenutoMultimediale(rs.getInt("idContenuto"), rs.getInt("idAccount"), rs.getString("titolo"),
                rs.getString("formato"), rs.getLong("dimensione"), Paths.get(rs.getString("filePath")), rs.getInt("posizione"));
    }

    @Override
    public void eliminaContenuto(int contenutoId) throws SQLException, IOException {
        Path filePath = null;
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT filePath FROM ContenutoMultimediale WHERE idContenuto=?")) {
            ps.setInt(1, contenutoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) filePath = Paths.get(rs.getString(1));
            }
        }
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("DELETE FROM ContenutoMultimediale WHERE idContenuto=?")) {
            ps.setInt(1, contenutoId);
            ps.executeUpdate();
        }
        if (filePath != null) Files.deleteIfExists(filePath);
    }

    @Override
    public void salvaOrdineContenuti(List<ContenutoMultimediale> contenuti) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("UPDATE ContenutoMultimediale SET posizione=? WHERE idContenuto=?")) {
            int pos = 1;
            for (ContenutoMultimediale item : contenuti) {
                ps.setInt(1, pos++);
                ps.setInt(2, item.idContenuto());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public LinkDiCondivisione salvaLinkDiCondivisione(int idAccount, List<Integer> contenutiIds, String descrizione, LocalDate dataDiScadenza) throws SQLException {
        String url = "AFAM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement("""
                    INSERT INTO LinkCondivisione(idStudente,url,descrizione,dataDiScadenza,numeroVisualizzazioni)
                    VALUES(?,?,?,?,0)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idAccount);
                ps.setString(2, url);
                ps.setString(3, descrizione == null ? "" : descrizione);
                ps.setString(4, dataDiScadenza == null ? null : dataDiScadenza.toString());
                ps.executeUpdate();
                int linkId;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Impossibile generare il link.");
                    linkId = keys.getInt(1);
                }
                try (PreparedStatement ps2 = c.prepareStatement("INSERT INTO ContenutiVisibili(linkId,contentId) VALUES(?,?)")) {
                    for (int id : contenutiIds) {
                        ps2.setInt(1, linkId);
                        ps2.setInt(2, id);
                        ps2.addBatch();
                    }
                    ps2.executeBatch();
                }
                c.commit();
                return new LinkDiCondivisione(linkId, idAccount, url, descrizione, dataDiScadenza, 0);
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    @Override
    public List<LinkDiCondivisione> recuperaLinkDiCondivisione(int idAccount) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM LinkCondivisione WHERE idStudente=? ORDER BY createdAt DESC")) {
            ps.setInt(1, idAccount);
            try (ResultSet rs = ps.executeQuery()) {
                List<LinkDiCondivisione> out = new ArrayList<>();
                while (rs.next()) out.add(linkFrom(rs));
                return out;
            }
        }
    }

    @Override
    public Optional<LinkDiCondivisione> recuperaLinkValido(String url) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT * FROM LinkCondivisione WHERE url=?")) {
            ps.setString(1, url);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                LinkDiCondivisione link = linkFrom(rs);
                if (link.dataDiScadenza() != null && link.dataDiScadenza().isBefore(LocalDate.now())) return Optional.empty();
                return Optional.of(link);
            }
        }
    }

    private LinkDiCondivisione linkFrom(ResultSet rs) throws SQLException {
        String date = rs.getString("dataDiScadenza");
        LocalDate expires = (date == null || date.isBlank()) ? null : LocalDate.parse(date);
        return new LinkDiCondivisione(rs.getInt("idLink"), rs.getInt("idStudente"), rs.getString("url"),
                rs.getString("descrizione"), expires, rs.getInt("numeroVisualizzazioni"));
    }

    @Override
    public void registraVisualizzazione(int linkId, String etichettaVisualizzatore) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(
                "UPDATE LinkCondivisione SET numeroVisualizzazioni=numeroVisualizzazioni+1 WHERE idLink=?")) {
            ps.setInt(1, linkId);
            ps.executeUpdate();
        }
    }

    @Override
    public void disattivaLink(int linkId) throws SQLException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("DELETE FROM LinkCondivisione WHERE idLink=?")) {
            ps.setInt(1, linkId);
            ps.executeUpdate();
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max);
    }
}
