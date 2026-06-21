package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.application.session.SessioneCorrente;
import afam.domain.entity.AccountStudente;
import afam.domain.repository.BoundaryDBMS;
import afam.domain.provider.BoundaryProviderEsterno;

public class AutenticazioneEsternaControl {
    private final BoundaryDBMS boundaryDBMS;
    private final BoundaryProviderEsterno boundaryProviderEsterno;
    private final SessioneCorrente sessioneCorrente;

    public AutenticazioneEsternaControl(BoundaryDBMS boundaryDBMS, BoundaryProviderEsterno boundaryProviderEsterno, SessioneCorrente sessioneCorrente) {
        this.boundaryDBMS = boundaryDBMS;
        this.boundaryProviderEsterno = boundaryProviderEsterno;
        this.sessioneCorrente = sessioneCorrente;
    }

    public AccountStudente autenticaConProviderEsterno(String email) throws Exception {
        String normalizedEmail = ValidazioneSupportControl.normalizzaEmail(email);
        ValidazioneSupportControl.richiediEmailValida(normalizedEmail);
        if (!boundaryProviderEsterno.autenticaStudente(normalizedEmail)) {
            throw new ApplicationException("Autenticazione tramite provider esterno fallita o annullata.");
        }
        AccountStudente account = boundaryDBMS.recuperaOCreaAccountProvider(normalizedEmail);
        boundaryDBMS.aggiornaStatoLogin(account.idAccount(), true);
        sessioneCorrente.avviaSessione(account);
        return account;
    }
}
