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
        Label testoMessaggio = new Label(messaggio);
        testoMessaggio.setWrapText(true);
        testoMessaggio.setMaxWidth(Double.MAX_VALUE);
        this.pulsanteOk = ui.button("OK", "primary");
        VBox panel = new VBox(14, testoMessaggio, pulsanteOk);
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
