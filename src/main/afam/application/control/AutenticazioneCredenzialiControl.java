package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.application.session.SessioneCorrente;
import afam.domain.entity.AccountStudente;
import afam.domain.repository.BoundaryDBMS;

import java.util.Optional;

public class AutenticazioneCredenzialiControl {
    private final BoundaryDBMS boundaryDBMS;
    private final SessioneCorrente sessioneCorrente;

    public AutenticazioneCredenzialiControl(BoundaryDBMS boundaryDBMS, SessioneCorrente sessioneCorrente) {
        this.boundaryDBMS = boundaryDBMS;
        this.sessioneCorrente = sessioneCorrente;
    }

    public AccountStudente autentica(String email, String password) throws Exception {
        String normalizedEmail = ValidazioneSupportControl.normalizzaEmail(email);
        ValidazioneSupportControl.richiediEmailValida(normalizedEmail);
        ValidazioneSupportControl.richiediTesto(password, "Inserisci la password.");
        Optional<AccountStudente> account = boundaryDBMS.autentica(normalizedEmail, password);
        if (account.isEmpty()) {
            throw new ApplicationException("Email o password errate!");
        }
        sessioneCorrente.avviaSessione(account.get());
        return account.get();
    }
}
