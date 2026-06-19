package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PannelloParametriFacoltativi {
    private final Parent root;
    private final TextArea campoDescrizione;
    private final DatePicker campoDataDiScadenza;
    private final Button pulsanteConferma;
    private final Button pulsanteAnnulla;

    public PannelloParametriFacoltativi(UiFactory ui) {
        Label titolo = new Label("Parametri facoltativi");
        titolo.getStyleClass().add("popup-title");
        this.campoDescrizione = ui.limitedTextArea("Descrizione", "", 200);
        this.campoDataDiScadenza = new DatePicker();
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        VBox panel = new VBox(14, titolo,
                new Label("Descrizione"), campoDescrizione,
                new Label("Data di scadenza"), campoDataDiScadenza,
                new HBox(10, pulsanteConferma, pulsanteAnnulla));
        panel.getStyleClass().add("popup-card");
        this.root = panel;
    }

    public Parent mostra() { return root; }
    public String descrizione() { return campoDescrizione.getText(); }
    public java.time.LocalDate dataDiScadenza() { return campoDataDiScadenza.getValue(); }
    public Button pulsanteConferma() { return pulsanteConferma; }
    public Button pulsanteAnnulla() { return pulsanteAnnulla; }
}
