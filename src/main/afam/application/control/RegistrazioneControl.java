package afam.application.control;

import afam.application.dto.SystemException;
import afam.domain.repository.BoundaryDBMS;

public class RegistrazioneControl {
    private final BoundaryDBMS boundaryDBMS;

    public RegistrazioneControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public void registra(String email, String password, String confermaPassword) throws Exception {
        
        String normalizedEmail = ValidazioneSupportControl.normalizzaEmail(email);
        ValidazioneSupportControl.richiediEmailValida(normalizedEmail);
        ValidazioneSupportControl.richiediTesto(password, "Inserisci la password.");
        ValidazioneSupportControl.richiediTesto(confermaPassword, "Inserisci la conferma password.");
        if (!password.equals(confermaPassword)) {
            throw new SystemException("Le due password non corrispondono.");
        }
        ValidazioneSupportControl.richiediPasswordSicura(password);
        if (boundaryDBMS.emailEsiste(normalizedEmail)) {
            throw new SystemException("L'email inserita è già associata a un utente!");
        }
        boundaryDBMS.salvaNuovoAccount(normalizedEmail, password);
        
    }
}
