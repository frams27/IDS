package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.domain.entity.AccountStudente;
import afam.domain.repository.BoundaryDBMS;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class PasswordDimenticataControl {
    private final BoundaryDBMS boundaryDBMS;

    public PasswordDimenticataControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public AccountStudente richiediRecuperoPassword(String email) throws Exception {
        String normalizedEmail = ValidazioneControlSupport.normalizzaEmail(email);
        ValidazioneControlSupport.richiediEmailValida(normalizedEmail);
        Optional<AccountStudente> account = boundaryDBMS.cercaAccountPerEmail(normalizedEmail);
        if (account.isEmpty()) {
            throw new ApplicationException("ATTENZIONE: l'email inserita non è valida.");
        }
        return account.get();
    }

    public String generaLinkRipristino(AccountStudente account) throws SQLException {
        String token = UUID.randomUUID().toString();
        boundaryDBMS.salvaTokenRipristino(account.idAccount(), token);
        return token;
    }

    public void impostaNuovaPassword(AccountStudente account, String token, String nuovaPassword, String confermaNuovaPassword) throws Exception {
        ValidazioneControlSupport.richiediTesto(nuovaPassword, "Inserisci la nuova password.");
        ValidazioneControlSupport.richiediTesto(confermaNuovaPassword, "Inserisci la conferma della nuova password.");
        if (!boundaryDBMS.verificaTokenRipristino(account.idAccount(), token)) {
            throw new ApplicationException("Link di ripristino non valido.");
        }
        if (!nuovaPassword.equals(confermaNuovaPassword)) {
            throw new ApplicationException("ATTENZIONE: le due password non corrispondono!");
        }
        ValidazioneControlSupport.richiediPasswordSicura(nuovaPassword);
        boundaryDBMS.aggiornaPassword(account.idAccount(), nuovaPassword);
        boundaryDBMS.cancellaTokenRipristino(account.idAccount());
    }
}
