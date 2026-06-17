package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PannelloDiConferma {
    private final Parent root;
    private final Button pulsanteConferma;
    private final Button pulsanteAnnulla;

    public static PannelloDiConferma create(UiFactory ui, String messaggio) {
        return new PannelloDiConferma(ui, messaggio);
    }

    public PannelloDiConferma(UiFactory ui, String messaggio) {
        Label testoMessaggio = new Label(messaggio);
        testoMessaggio.setWrapText(true);
        testoMessaggio.setMaxWidth(Double.MAX_VALUE);
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        this.root = ui.page("Conferma", null, testoMessaggio, new HBox(10, pulsanteConferma, pulsanteAnnulla));
    }

    public Parent mostra() {
        return root;
    }

    public Parent getRoot() {
        return root;
    }

    public Button pulsanteConferma() {
        return pulsanteConferma;
    }

    public Button pulsanteAnnulla() {
        return pulsanteAnnulla;
    }
}
