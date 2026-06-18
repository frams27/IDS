package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class SchermataErroreConnessione {
    private final Parent root;
    private final Button pulsanteRiconnessione;

    public SchermataErroreConnessione(UiFactory ui) {
        this.pulsanteRiconnessione = ui.button("Riconnessione", "primary");
        this.root = ui.page("Nessuna connessione a Internet", null, pulsanteRiconnessione);
    }

    public Parent mostra() { return root; }
    public Button pulsanteRiconnessione() { return pulsanteRiconnessione; }
}
