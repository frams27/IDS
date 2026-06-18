package afam.domain.repository;

import afam.domain.entity.AccountStudente;
import afam.domain.entity.ContenutoMultimediale;
import afam.domain.entity.DatiCurriculari;
import afam.domain.entity.LinkDiCondivisione;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Boundary tecnica verso il DBMS.
 * Le control comunicano con questa interfaccia e non direttamente con SQLite.
 */
public interface BoundaryDBMS {
    void inizializza() throws Exception;
    Path cartellaUpload();

    boolean emailEsiste(String email) throws SQLException;
    void salvaNuovoAccount(String email, String password) throws SQLException;
    Optional<AccountStudente> cercaAccountPerEmail(String email) throws SQLException;
    AccountStudente recuperaOCreaAccountProvider(String email) throws SQLException;
    Optional<AccountStudente> autentica(String email, String password) throws SQLException;
    boolean verificaPassword(int accountStudenteId, String password) throws SQLException;
    void aggiornaPassword(int accountStudenteId, String nuovaPassword) throws SQLException;
    void aggiornaStatoLogin(int accountStudenteId, boolean logged) throws SQLException;


    void salvaTokenRipristino(int accountStudenteId, String token) throws SQLException;
    boolean verificaTokenRipristino(int accountStudenteId, String token) throws SQLException;
    void cancellaTokenRipristino(int accountStudenteId) throws SQLException;

    DatiCurriculari recuperaDatiCurriculari(int accountStudenteId) throws SQLException;
    void salvaDatiCurriculari(int accountStudenteId, String biografia, String titoliDiStudio, String esperienzeArtisticheEFormative) throws SQLException;

    void salvaContenuto(int accountStudenteId, String titolo, String tipo, String nomeOriginale, String formato, long dimensione, Path percorsoFile) throws SQLException;
    List<ContenutoMultimediale> recuperaContenuti(int accountStudenteId) throws SQLException;
    List<ContenutoMultimediale> recuperaContenutiPerLink(int linkId) throws SQLException;
    void eliminaContenuto(int contenutoId) throws SQLException, IOException;
    void salvaOrdineContenuti(List<ContenutoMultimediale> contenuti) throws SQLException;

    LinkDiCondivisione salvaLinkDiCondivisione(int accountStudenteId, List<Integer> contenutiIds, String descrizione, LocalDate dataDiScadenza) throws SQLException;
    List<LinkDiCondivisione> recuperaLinkDiCondivisione(int accountStudenteId, boolean soloAttivi) throws SQLException;
    Optional<LinkDiCondivisione> recuperaLinkValido(String url) throws SQLException;
    void registraVisualizzazione(int linkId, String etichettaVisualizzatore) throws SQLException;
    List<String> recuperaRigheVisualizzazione(int linkId) throws SQLException;
    void disattivaLink(int linkId) throws SQLException;
}
