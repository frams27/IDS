package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class PannelloParametriFacoltativi {
    private final Parent root;
    private final TextArea campoDescrizione;
    private final DatePicker campoDataDiScadenza;
    private final Button pulsanteConferma;

    public PannelloParametriFacoltativi(UiFactory ui, Runnable annullaOperazione) {
        this.campoDescrizione = ui.limitedTextArea("Descrizione", "", 200);
        this.campoDataDiScadenza = new DatePicker();
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.root = ui.page("Parametri facoltativi", annullaOperazione,
                new Label("Descrizione"), campoDescrizione,
                new Label("Data di scadenza"), campoDataDiScadenza,
                pulsanteConferma);
    }

    public Parent mostra() { return root; }
    public String descrizione() { return campoDescrizione.getText(); }
    public java.time.LocalDate dataDiScadenza() { return campoDataDiScadenza.getValue(); }
    public Button pulsanteConferma() { return pulsanteConferma; }
}
