package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PannelloDiNotifica {
    private final Button pulsanteOk;

    public PannelloDiNotifica(UiFactory ui) {
        this.pulsanteOk = ui.button("OK", "primary");
    }

    public Parent mostra(String messaggio) {
        Label titoloNotifica = new Label("Pannello di Notifica");
        titoloNotifica.getStyleClass().add("popup-title");
        Label testoMessaggio = new Label(messaggio);
        testoMessaggio.setWrapText(true);
        testoMessaggio.setMaxWidth(Double.MAX_VALUE);
        VBox panel = new VBox(14, titoloNotifica, testoMessaggio, pulsanteOk);
        panel.getStyleClass().add("popup-card");
        return panel;
    }


    public Button pulsanteOk() {
        return pulsanteOk;
    }
}
