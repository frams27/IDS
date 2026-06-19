package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.application.session.SessioneCorrente;
import afam.domain.entity.AccountStudente;
import afam.domain.repository.BoundaryDBMS;
import afam.util.SecurityUtil;

public class DueFAControl {
    private static final int MAX_TENTATIVI = 5;

    private record OtpSession(String codice, int idAccount, int tentativi) {
        OtpSession incrementaTentativi() {
            return new OtpSession(codice, idAccount, tentativi + 1);
        }
    }

    private final BoundaryDBMS boundaryDBMS;
    private final SessioneCorrente sessioneCorrente;
    private OtpSession otpSession = null;

    public DueFAControl(BoundaryDBMS boundaryDBMS, SessioneCorrente sessioneCorrente) {
        this.boundaryDBMS = boundaryDBMS;
        this.sessioneCorrente = sessioneCorrente;
    }

    public String generaOTP() throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        otpSession = new OtpSession(SecurityUtil.generateOtp(), account.idAccount(), 0);
        return otpSession.codice();
    }

    public void verificaOTP(String codice) throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        ValidazioneControlSupport.richiediTesto(codice, "Inserisci il codice OTP.");
        if (otpSession == null || otpSession.idAccount() != account.idAccount()) {
            throw new ApplicationException("Clicca sul pulsante Genera OTP per generare il codice");
        }
        otpSession = otpSession.incrementaTentativi();
        if (otpSession.codice().equals(codice.trim())) {
            boundaryDBMS.aggiornaStatoLogin(account.idAccount(), true);
            otpSession = null;
            return;
        }
        if (otpSession.tentativi() >= MAX_TENTATIVI) {
            otpSession = null;
            sessioneCorrente.terminaSessione();
            throw new ApplicationException("Numero massimo di tentativi raggiunto. Sessione annullata.");
        }
        throw new ApplicationException("Codice errato");
    }
}
