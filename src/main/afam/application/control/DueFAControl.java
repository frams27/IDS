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
    private String codiceOtpGenerato;
    private int idAccountOtpGenerato = -1; //per inizializzare senza valori garbage

    public DueFAControl(BoundaryDBMS boundaryDBMS, SessioneCorrente sessioneCorrente) {
        this.boundaryDBMS = boundaryDBMS;
        this.sessioneCorrente = sessioneCorrente;
    }

    public String generaOTP() throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        codiceOtpGenerato = SecurityUtil.generateOtp();
        idAccountOtpGenerato = account.idAccount(); //salvataggio acc per cui ha generato otp
        counterTentativi = 0;
        return codiceOtpGenerato;
    }

    public void verificaOTP(String codice) throws Exception {
        AccountStudente account = sessioneCorrente.richiediAccountStudente();
        ValidazioneControlSupport.richiediTesto(codice, "Inserisci il codice OTP.");
        if (codiceOtpGenerato == null || idAccountOtpGenerato != account.idAccount()) {
            throw new ApplicationException("Clicca sul pulsante Genera OTP per generare il codice");
        }
        counterTentativi++;
        if (codiceOtpGenerato.equals(codice.trim())) {
            boundaryDBMS.aggiornaStatoLogin(account.idAccount(), true);
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
        codiceOtpGenerato = null;
        idAccountOtpGenerato = -1;
        counterTentativi = 0;
    }
}
