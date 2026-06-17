package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.util.SecurityUtil;

import java.util.Locale;

final class ValidazioneControlSupport {
    private ValidazioneControlSupport() {}

    static String normalizzaEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    static void richiediTesto(String value, String message) throws ApplicationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ApplicationException(message);
        }
    }

    static void richiediEmailValida(String email) throws ApplicationException {
        richiediTesto(email, "Inserisci l'email.");
        if (!SecurityUtil.isValidEmail(email)) {
            throw new ApplicationException("Inserisci un indirizzo email valido.");
        }
    }

    static void richiediPasswordSicura(String password) throws ApplicationException {
        if (!SecurityUtil.isStrongPassword(password)) {
            throw new ApplicationException("La password deve contenere almeno una minuscola, una maiuscola, un numero e un carattere speciale.");
        }
    }

    static void verificaLimiteTesto(String value, String fieldName) throws ApplicationException {
        if (value != null && value.length() > 500) {
            throw new ApplicationException(fieldName + " supera il limite di 500 caratteri.");
        }
    }
}
