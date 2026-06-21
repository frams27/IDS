package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.util.SecurityUtil;

import java.util.Locale;

final class ValidazioneSupportControl {
    private ValidazioneSupportControl() {}

    static String normalizzaEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    static void richiediTesto(String value, String msg) throws ApplicationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ApplicationException(msg);
        }
    }

    static void richiediEmailValida(String email) throws ApplicationException {
        richiediTesto(email, "Inserisci l'email.");
        if (!SecurityUtil.isValidEmail(email)) {
            throw new ApplicationException("ATTENZIONE: l\u2019email inserita non \u00e8 valida.");
        }
    }

    static void richiediPasswordSicura(String password) throws ApplicationException {
        if (!SecurityUtil.isStrongPassword(password)) {
            throw new ApplicationException("La password dell'account deve contenere almeno una lettera minuscola, una lettera maiuscola, un numero e un carattere speciale (es. “.”, “!”, “&”).");
        }
    }

    static void verificaLimiteTesto(String value, String fieldName) throws ApplicationException {
        if (value != null && value.length() > 200) {
            throw new ApplicationException(fieldName + " supera il limite di 200 caratteri.");
        }
    }
}
