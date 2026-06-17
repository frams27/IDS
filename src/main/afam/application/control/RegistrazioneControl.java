package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.domain.repository.BoundaryDBMS;

public class RegistrazioneControl {
    private final BoundaryDBMS boundaryDBMS;

    public RegistrazioneControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public void registra(String email, String password, String confermaPassword) throws Exception {
        String normalizedEmail = ValidazioneControlSupport.normalizzaEmail(email);
        ValidazioneControlSupport.richiediEmailValida(normalizedEmail);
        ValidazioneControlSupport.richiediTesto(password, "Inserisci la password.");
        ValidazioneControlSupport.richiediTesto(confermaPassword, "Inserisci la conferma password.");
        if (!password.equals(confermaPassword)) {
            throw new ApplicationException("Le due password non corrispondono.");
        }
        ValidazioneControlSupport.richiediPasswordSicura(password);
        if (boundaryDBMS.emailEsiste(normalizedEmail)) {
            throw new ApplicationException("L'email inserita è già associata a un utente.");
        }
        boundaryDBMS.salvaNuovoAccount(normalizedEmail, password);
    }
}
