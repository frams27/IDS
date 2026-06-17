package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SchermataGestioneCondivisione {
    private final Parent root;
    private final Button pulsanteGeneraLink;
    private final Button pulsanteFeedbackContenuti;
    private final Button pulsanteDisattivaLink;

    public static SchermataGestioneCondivisione create(UiFactory ui, Runnable tornaAllaHomePage) {
        return new SchermataGestioneCondivisione(ui, tornaAllaHomePage);
    }

    public SchermataGestioneCondivisione(UiFactory ui, Runnable tornaAllaHomePage) {
        this.pulsanteGeneraLink = ui.button("Genera link", "primary");
        this.pulsanteFeedbackContenuti = ui.button("Feedback contenuti", "secondary");
        this.pulsanteDisattivaLink = ui.button("Disattiva link", "danger");
        this.root = ui.page("Gestione condivisione", tornaAllaHomePage,
                new VBox(12, pulsanteGeneraLink, pulsanteFeedbackContenuti, pulsanteDisattivaLink));
    }

    public Parent mostra() { return root; }
    public Parent getRoot() { return root; }
    public Button pulsanteGeneraLink() { return pulsanteGeneraLink; }
    public Button pulsanteFeedbackContenuti() { return pulsanteFeedbackContenuti; }
    public Button pulsanteDisattivaLink() { return pulsanteDisattivaLink; }
}
