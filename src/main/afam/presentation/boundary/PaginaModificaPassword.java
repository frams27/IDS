package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;

public class PaginaModificaPassword {
    private final Parent root;
    private final PasswordField campoVecchiaPassword;
    private final PasswordField campoNuovaPassword;
    private final PasswordField campoConfermaNuovaPassword;
    private final Button pulsanteConferma;
    private final Button pulsanteAnnulla;

    public PaginaModificaPassword(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        this.campoVecchiaPassword = ui.passwordField("Vecchia password");
        this.campoNuovaPassword = ui.passwordField("Nuova password");
        this.campoConfermaNuovaPassword = ui.passwordField("Conferma nuova password");
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        this.root = ui.page("Modifica password", tornaAllaGestioneProfilo,
                new Label("Vecchia password"), campoVecchiaPassword,
                new Label("Nuova password"), campoNuovaPassword,
                new Label("Conferma nuova password"), campoConfermaNuovaPassword,
                new HBox(10, pulsanteConferma, pulsanteAnnulla));
    }

    public Parent mostra() { return root; }
    public String vecchiaPassword() { return campoVecchiaPassword.getText(); }
    public String nuovaPassword() { return campoNuovaPassword.getText(); }
    public String confermaNuovaPassword() { return campoConfermaNuovaPassword.getText(); }
    public Button pulsanteConferma() { return pulsanteConferma; }
    public Button pulsanteAnnulla() { return pulsanteAnnulla; }
}
