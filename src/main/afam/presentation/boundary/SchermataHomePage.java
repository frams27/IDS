package afam.presentation.boundary;

import afam.domain.entity.AccountStudente;
import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SchermataHomePage {
    private final UiFactory ui;
    private Parent root;
    private Button pulsanteGestioneProfilo;
    private Button pulsanteGestioneCondivisione;
    private Button pulsanteLogout;

    public SchermataHomePage(UiFactory ui) {
        this.ui = ui;
    }

    public Parent mostra(AccountStudente account) {
        this.pulsanteGestioneProfilo = ui.button("Gestione profilo", "primary");
        this.pulsanteGestioneCondivisione = ui.button("Gestione condivisione", "primary");
        this.pulsanteLogout = ui.button("Logout", "secondary");
        this.root = ui.page("Home page", null,
                new Label("Studente: " + account.email()),
                new VBox(12, pulsanteGestioneProfilo, pulsanteGestioneCondivisione, pulsanteLogout));
        return root;
    }

    public Button pulsanteGestioneProfilo() { return pulsanteGestioneProfilo; }
    public Button pulsanteGestioneCondivisione() { return pulsanteGestioneCondivisione; }
    public Button pulsanteLogout() { return pulsanteLogout; }
}
