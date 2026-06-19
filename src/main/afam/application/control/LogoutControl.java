package afam.application.control;

import afam.application.session.SessioneCorrente;
import afam.domain.repository.BoundaryDBMS;

public class LogoutControl {
    private final BoundaryDBMS boundaryDBMS;
    private final SessioneCorrente sessioneCorrente;

    public LogoutControl(BoundaryDBMS boundaryDBMS, SessioneCorrente sessioneCorrente) {
        this.boundaryDBMS = boundaryDBMS;
        this.sessioneCorrente = sessioneCorrente;
    }

    public void eseguiLogout() throws Exception {
        if (sessioneCorrente.accountStudente().isPresent()) {
            boundaryDBMS.aggiornaStatoLogin(sessioneCorrente.accountStudente().get().idAccount(), false);
        }
        sessioneCorrente.terminaSessione();
    }
}
