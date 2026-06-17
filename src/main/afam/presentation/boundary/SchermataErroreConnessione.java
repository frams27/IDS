package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class SchermataErroreConnessione {
    private final Parent root;
    private final Button pulsanteRiconnessione;

    public static SchermataErroreConnessione create(UiFactory ui) {
        return new SchermataErroreConnessione(ui);
    }

    public SchermataErroreConnessione(UiFactory ui) {
        this.pulsanteRiconnessione = ui.button("Riprova connessione", "primary");
        this.root = ui.page("Nessuna connessione a Internet", null, pulsanteRiconnessione);
    }

    public Parent mostra() { return root; }
    public Parent getRoot() { return root; }
    public Button pulsanteRiconnessione() { return pulsanteRiconnessione; }
}
