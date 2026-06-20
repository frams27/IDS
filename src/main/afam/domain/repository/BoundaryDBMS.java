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

/* Le control comunicano con questa interfaccia e non direttamente con SQLite. 
    elenca cosa si può chiedere al database, senza dire il come. */

public interface BoundaryDBMS {
    void inizializza() throws Exception; //Crea le tabelle se non esistono e prepara l'ambiente. Può lanciare eccezioni.
    Path cartellaUpload(); // Restituisce il percorso della cartella dove salvare i file caricati.

    boolean emailEsiste(String email) throws SQLException;
    void salvaNuovoAccount(String email, String password) throws SQLException;
    Optional<AccountStudente> cercaAccountPerEmail(String email) throws SQLException;
    AccountStudente recuperaOCreaAccountProvider(String email) throws SQLException;
    Optional<AccountStudente> autentica(String email, String password) throws SQLException;
    boolean verificaPassword(int idAccount, String password) throws SQLException;
    void aggiornaPassword(int idAccount, String nuovaPassword) throws SQLException;
    void aggiornaStatoLogin(int idAccount, boolean logged) throws SQLException;


    void salvaTokenRipristino(int idAccount, String token) throws SQLException;
    boolean verificaTokenRipristino(int idAccount, String token) throws SQLException;
    void cancellaTokenRipristino(int idAccount) throws SQLException;

    DatiCurriculari recuperaDatiCurriculari(int idAccount) throws SQLException;
    void salvaDatiCurriculari(int idAccount, String biografia, String titoliDiStudio, String esperienzeArtisticheEFormative) throws SQLException;

    void salvaContenuto(int idAccount, String titolo, String formato, long dimensione, Path percorsoFile) throws SQLException;
    List<ContenutoMultimediale> recuperaContenuti(int idAccount) throws SQLException;
    List<ContenutoMultimediale> recuperaContenutiPerLink(int linkId) throws SQLException;
    void eliminaContenuto(int contenutoId) throws SQLException, IOException;
    void salvaOrdineContenuti(List<ContenutoMultimediale> contenuti) throws SQLException;

    LinkDiCondivisione salvaLinkDiCondivisione(int idAccount, List<Integer> contenutiIds, String descrizione, LocalDate dataDiScadenza) throws SQLException;
    List<LinkDiCondivisione> recuperaLinkDiCondivisione(int idAccount) throws SQLException;
    Optional<LinkDiCondivisione> recuperaLinkValido(String url) throws SQLException;
    void registraVisualizzazione(int linkId, String etichettaVisualizzatore) throws SQLException;
    void disattivaLink(int linkId) throws SQLException;
}
