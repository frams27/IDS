package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.application.session.SessioneCorrente;
import afam.domain.entity.AccountStudente;
import afam.domain.repository.BoundaryDBMS;

public class ModificaPasswordControl {
    private final BoundaryDBMS boundaryDBMS;
    private final SessioneCorrente sessioneCorrente;

    public ModificaPasswordControl(BoundaryDBMS boundaryDBMS, SessioneCorrente sessioneCorrente) {
        this.boundaryDBMS = boundaryDBMS;
        this.sessioneCorrente = sessioneCorrente;
    }

    public void modificaPassword(String vecchiaPassword, String nuovaPassword, String confermaNuovaPassword) throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        ValidazioneControlSupport.richiediTesto(vecchiaPassword, "Inserisci la vecchia password.");
        ValidazioneControlSupport.richiediTesto(nuovaPassword, "Inserisci la nuova password.");
        ValidazioneControlSupport.richiediTesto(confermaNuovaPassword, "Inserisci la conferma della nuova password.");
        if (!boundaryDBMS.verificaPassword(account.id(), vecchiaPassword)) {
            throw new ApplicationException("La password attuale è errata.");
        }
        if (!nuovaPassword.equals(confermaNuovaPassword)) {
            throw new ApplicationException("Le nuove password non corrispondono.");
        }
        if (vecchiaPassword.equals(nuovaPassword)) {
            throw new ApplicationException("La nuova password non può essere uguale a quella attuale.");
        }
        ValidazioneControlSupport.richiediPasswordSicura(nuovaPassword);
        boundaryDBMS.aggiornaPassword(account.id(), nuovaPassword);
    }
}
