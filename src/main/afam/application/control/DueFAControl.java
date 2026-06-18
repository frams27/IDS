package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.application.session.SessioneCorrente;
import afam.domain.entity.AccountStudente;
import afam.domain.repository.BoundaryDBMS;
import afam.util.SecurityUtil;

public class DueFAControl {
    private final BoundaryDBMS boundaryDBMS;
    private final SessioneCorrente sessioneCorrente;
    private int counterTentativi;

    public DueFAControl(BoundaryDBMS boundaryDBMS, SessioneCorrente sessioneCorrente) {
        this.boundaryDBMS = boundaryDBMS;
        this.sessioneCorrente = sessioneCorrente;
    }

    public String generaOTP() throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        String otp = SecurityUtil.generateOtp();
        boundaryDBMS.salvaOTP(account.id(), otp);
        counterTentativi = 0;
        return otp;
    }

    public void verificaOTP(String codice) throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        ValidazioneControlSupport.richiediTesto(codice, "Inserisci il codice OTP.");
        counterTentativi++;
        if (boundaryDBMS.verificaOTP(account.id(), codice.trim())) {
            boundaryDBMS.aggiornaStatoLogin(account.id(), true);
            counterTentativi = 0;
            return;
        }
        if (counterTentativi >= 5) {
            sessioneCorrente.terminaSessione();
            counterTentativi = 0;
            throw new ApplicationException("Numero massimo di tentativi raggiunto. Sessione annullata.");
        }
        throw new ApplicationException("Codice errato");
    }
}
