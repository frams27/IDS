package afam.application.control;

import afam.application.dto.SystemException;
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
        ValidazioneSupportControl.richiediTesto(vecchiaPassword, "Inserisci la vecchia password.");
        ValidazioneSupportControl.richiediTesto(nuovaPassword, "Inserisci la nuova password.");
        ValidazioneSupportControl.richiediTesto(confermaNuovaPassword, "Inserisci la conferma della nuova password.");
        if (!boundaryDBMS.verificaPassword(account.idAccount(), vecchiaPassword)) {
            throw new SystemException("Password attuale errata");
        }
        if (!nuovaPassword.equals(confermaNuovaPassword)) {
            throw new SystemException("Le nuove password non corrispondono");
        }
        if (vecchiaPassword.equals(nuovaPassword)) {
            throw new SystemException("La nuova password non può essere uguale a quella attuale");
        }
        ValidazioneSupportControl.richiediPasswordSicura(nuovaPassword);
        boundaryDBMS.aggiornaPassword(account.idAccount(), nuovaPassword);
    }
}
