package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PannelloDiNotifica {
    private final Parent root;
    private final Button pulsanteOk;

    public PannelloDiNotifica(UiFactory ui, String messaggio) {
        //qui inseriramo il titolo del pannello di notifica
        Label titoloNotifica = new Label("Pannello di Notifica");
        titoloNotifica.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        //qui inseriamo il messaggio di notifica
        Label testoMessaggio = new Label(messaggio);
        testoMessaggio.setWrapText(true);
        testoMessaggio.setMaxWidth(Double.MAX_VALUE);
        this.pulsanteOk = ui.button("OK", "primary");
        VBox panel = new VBox(14, titoloNotifica, testoMessaggio, pulsanteOk);
        panel.getStyleClass().add("popup-card");
        this.root = panel;
    }

    public Parent mostra() {
        return root;
    }


    public Button pulsanteOk() {
        return pulsanteOk;
    }
}
