package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class PaginaNuovaPassword {
    private final Parent root;
    private final PasswordField campoNuovaPassword;
    private final PasswordField campoConfermaNuovaPassword;
    private final Button pulsanteConferma;

    public PaginaNuovaPassword(UiFactory ui, Runnable tornaAllaPaginaDiLogin) {
        this.campoNuovaPassword = ui.passwordField("Nuova password");
        this.campoConfermaNuovaPassword = ui.passwordField("Conferma nuova password");
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.root = ui.page("Pagina nuova password", tornaAllaPaginaDiLogin,
                new Label("Nuova password"), campoNuovaPassword,
                new Label("Conferma nuova password"), campoConfermaNuovaPassword,
                pulsanteConferma);
    }

    public Parent mostra() { return root; }
    public String nuovaPassword() { return campoNuovaPassword.getText(); }
    public String confermaNuovaPassword() { return campoConfermaNuovaPassword.getText(); }
    public Button pulsanteConferma() { return pulsanteConferma; }
}
