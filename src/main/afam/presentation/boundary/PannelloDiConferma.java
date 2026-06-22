package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PannelloDiConferma {
    private final Button pulsanteConferma;
    private final Button pulsanteAnnulla;

    public PannelloDiConferma(UiFactory ui) {
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
    }

    public Parent mostra(String msg) {
        Label titoloConferma = new Label("Pannello di Conferma");
        titoloConferma.getStyleClass().add("popup-title");
        Label testoMessaggio = new Label(msg);
        testoMessaggio.setWrapText(true);
        testoMessaggio.setMaxWidth(Double.MAX_VALUE);
        VBox panel = new VBox(14, titoloConferma, testoMessaggio, new HBox(10, pulsanteConferma, pulsanteAnnulla));
        panel.getStyleClass().add("popup-card");
        return panel;
    }


    public Button pulsanteConferma() {
        return pulsanteConferma;
    }

    public Button pulsanteAnnulla() {
        return pulsanteAnnulla;
    }
}
