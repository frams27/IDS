package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PaginaDiLogin {
    private final Parent root;
    private final TextField campoEmail;
    private final PasswordField campoPassword;
    private final Button pulsanteAccessoConCredenziali;
    private final Button pulsanteLoginEsterno;
    private final Button pulsantePasswordDimenticata;

    public static PaginaDiLogin create(UiFactory ui, Runnable tornaAllaStartingPage) {
        return new PaginaDiLogin(ui, tornaAllaStartingPage);
    }

    public PaginaDiLogin(UiFactory ui, Runnable tornaAllaStartingPage) {
        this.campoEmail = ui.field("Email");
        this.campoPassword = ui.passwordField("Password");
        this.pulsanteAccessoConCredenziali = ui.button("Accedi con credenziali", "primary");
        this.pulsanteLoginEsterno = ui.button("Login esterno", "secondary");
        this.pulsantePasswordDimenticata = ui.button("Password dimenticata", "secondary");
        this.root = ui.page("Pagina di login", tornaAllaStartingPage,
                new Label("Email"), campoEmail,
                new Label("Password"), campoPassword,
                new HBox(10, pulsanteAccessoConCredenziali, pulsanteLoginEsterno, pulsantePasswordDimenticata));
    }

    public Parent mostra() { return root; }
    public Parent getRoot() { return root; }
    public String email() { return campoEmail.getText(); }
    public String password() { return campoPassword.getText(); }
    public Button pulsanteAccessoConCredenziali() { return pulsanteAccessoConCredenziali; }
    public Button pulsanteLoginEsterno() { return pulsanteLoginEsterno; }
    public Button pulsantePasswordDimenticata() { return pulsantePasswordDimenticata; }
}
