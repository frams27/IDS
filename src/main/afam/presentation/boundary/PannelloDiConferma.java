package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PannelloDiConferma {
    private final Parent root;
    private final Button pulsanteConferma;
    private final Button pulsanteAnnulla;

    public PannelloDiConferma(UiFactory ui, String messaggio) {
        Label testoMessaggio = new Label(messaggio);
        testoMessaggio.setWrapText(true);
        testoMessaggio.setMaxWidth(Double.MAX_VALUE);
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        VBox panel = new VBox(14, testoMessaggio, new HBox(10, pulsanteConferma, pulsanteAnnulla));
        panel.getStyleClass().add("popup-card");
        this.root = panel;
    }

    public Parent mostra() {
        return root;
    }


    public Button pulsanteConferma() {
        return pulsanteConferma;
    }

    public Button pulsanteAnnulla() {
        return pulsanteAnnulla;
    }
}
