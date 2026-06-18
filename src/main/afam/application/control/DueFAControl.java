package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.application.session.SessioneCorrente;
import afam.domain.entity.AccountStudente;
import afam.domain.repository.BoundaryDBMS;
import afam.util.SecurityUtil;

public class DueFAControl {
    private static final int MAX_TENTATIVI = 5;

    private final BoundaryDBMS boundaryDBMS;
    private final SessioneCorrente sessioneCorrente;
    private int counterTentativi;
    private String otpCorrente;
    private AccountStudente accountOtp;

    public DueFAControl(BoundaryDBMS boundaryDBMS, SessioneCorrente sessioneCorrente) {
        this.boundaryDBMS = boundaryDBMS;
        this.sessioneCorrente = sessioneCorrente;
    }

    public String generaOTP() throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        otpCorrente = SecurityUtil.generateOtp();
        accountOtp = account;
        counterTentativi = 0;
        return otpCorrente;
    }

    public void verificaOTP(String codice) throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        ValidazioneControlSupport.richiediTesto(codice, "Inserisci il codice OTP.");
        if (otpCorrente == null || accountOtp != account) {
            throw new ApplicationException("Genera prima un codice OTP.");
        }
        counterTentativi++;
        if (otpCorrente.equals(codice.trim())) {
            boundaryDBMS.aggiornaStatoLogin(account.id(), true);
            cancellaOTP();
            return;
        }
        if (counterTentativi >= MAX_TENTATIVI) {
            sessioneCorrente.terminaSessione();
            cancellaOTP();
            throw new ApplicationException("Numero massimo di tentativi raggiunto. Sessione annullata.");
        }
        throw new ApplicationException("Codice errato");
    }

    private void cancellaOTP() {
        otpCorrente = null;
        accountOtp = null;
        counterTentativi = 0;
    }
}
