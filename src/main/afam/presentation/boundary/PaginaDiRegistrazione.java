package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PaginaDiRegistrazione {
    private final Parent root;
    private final TextField campoEmail;
    private final PasswordField campoPassword;
    private final PasswordField campoConfermaPassword;
    private final Button pulsanteConfermaRegistrazione;

    public PaginaDiRegistrazione(UiFactory ui, Runnable tornaAllaStartingPage) {
        this.campoEmail = ui.field("Email");
        this.campoPassword = ui.passwordField("Password");
        this.campoConfermaPassword = ui.passwordField("Conferma password");
        this.pulsanteConfermaRegistrazione = ui.button("Conferma registrazione", "primary");
        this.root = ui.page("Pagina di registrazione", tornaAllaStartingPage,
                new Label("Email"), campoEmail,
                new Label("Password"), campoPassword,
                new Label("Conferma password"), campoConfermaPassword,
                pulsanteConfermaRegistrazione);
    }

    public Parent mostra() { return root; }
    public String email() { return campoEmail.getText(); }
    public String password() { return campoPassword.getText(); }
    public String confermaPassword() { return campoConfermaPassword.getText(); }
    public Button pulsanteConfermaRegistrazione() { return pulsanteConfermaRegistrazione; }
}
