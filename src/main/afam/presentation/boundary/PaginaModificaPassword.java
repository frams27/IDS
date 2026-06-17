package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class PaginaModificaPassword {
    private final Parent root;
    private final PasswordField campoVecchiaPassword;
    private final PasswordField campoNuovaPassword;
    private final PasswordField campoConfermaNuovaPassword;
    private final Button pulsanteConferma;

    public static PaginaModificaPassword create(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        return new PaginaModificaPassword(ui, tornaAllaGestioneProfilo);
    }

    public PaginaModificaPassword(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        this.campoVecchiaPassword = ui.passwordField("Vecchia password");
        this.campoNuovaPassword = ui.passwordField("Nuova password");
        this.campoConfermaNuovaPassword = ui.passwordField("Conferma nuova password");
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.root = ui.page("Modifica password", tornaAllaGestioneProfilo,
                new Label("Vecchia password"), campoVecchiaPassword,
                new Label("Nuova password"), campoNuovaPassword,
                new Label("Conferma nuova password"), campoConfermaNuovaPassword,
                pulsanteConferma);
    }

    public Parent mostra() { return root; }
    public Parent getRoot() { return root; }
    public String vecchiaPassword() { return campoVecchiaPassword.getText(); }
    public String nuovaPassword() { return campoNuovaPassword.getText(); }
    public String confermaNuovaPassword() { return campoConfermaNuovaPassword.getText(); }
    public Button pulsanteConferma() { return pulsanteConferma; }
}
