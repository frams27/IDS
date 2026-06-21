package afam.application.control;

import afam.application.dto.SystemException;
import afam.util.SecurityUtil;

import java.util.Locale;

final class ValidazioneSupportControl {
    private ValidazioneSupportControl() {}

    static String normalizzaEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    static void richiediTesto(String value, String msg) throws SystemException {
        if (value == null || value.trim().isEmpty()) {
            throw new SystemException(msg);
        }
    }

    static void richiediEmailValida(String email) throws SystemException {
        richiediTesto(email, "Inserisci l'email.");
        if (!SecurityUtil.isValidEmail(email)) {
            throw new SystemException("ATTENZIONE: l\u2019email inserita non \u00e8 valida.");
        }
    }

    static void richiediPasswordSicura(String password) throws SystemException {
        if (!SecurityUtil.isStrongPassword(password)) {
            throw new SystemException("La password dell'account deve contenere almeno una lettera minuscola, una lettera maiuscola, un numero e un carattere speciale (es. “.”, “!”, “&”).");
        }
    }

    static void verificaLimiteTesto(String value, String fieldName) throws SystemException {
        if (value != null && value.length() > 200) {
            throw new SystemException(fieldName + " supera il limite di 200 caratteri.");
        }
    }
}
