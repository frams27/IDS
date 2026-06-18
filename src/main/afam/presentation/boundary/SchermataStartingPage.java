package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SchermataStartingPage {
    private final Parent root;
    private final Button pulsanteRegistrati;
    private final Button pulsanteAccedi;
    private final Button pulsanteEntraComeUtenteEsterno;

    public SchermataStartingPage(UiFactory ui) {
        this.pulsanteRegistrati = ui.button("Registrati", "primary");
        this.pulsanteAccedi = ui.button("Accedi", "primary");
        this.pulsanteEntraComeUtenteEsterno = ui.button("Entra come utente esterno", "secondary");
        VBox buttons = new VBox(12, pulsanteRegistrati, pulsanteAccedi, pulsanteEntraComeUtenteEsterno);
        buttons.setAlignment(Pos.CENTER);
        buttons.setMaxWidth(360);
        this.root = ui.page("Identità Digitale AFAM", null, buttons);
    }

    public Parent mostra() { return root; }
    public Button pulsanteRegistrati() { return pulsanteRegistrati; }
    public Button pulsanteAccedi() { return pulsanteAccedi; }
    public Button pulsanteEntraComeUtenteEsterno() { return pulsanteEntraComeUtenteEsterno; }
}
