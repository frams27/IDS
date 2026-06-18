package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PannelloAccessoLink {
    private final Parent root;
    private final TextField campoLink;
    private final Button pulsanteConferma;
    private final Button pulsanteAnnulla;

    public PannelloAccessoLink(UiFactory ui, Runnable tornaAllaStartingPage) {
        this.campoLink = ui.field("Inserisci il link di condivisione");
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        this.root = ui.page("Accesso tramite link", tornaAllaStartingPage,
                campoLink, new HBox(10, pulsanteConferma, pulsanteAnnulla));
    }

    public Parent mostra() { return root; }
    public String linkInserito() { return campoLink.getText(); }
    public Button pulsanteConferma() { return pulsanteConferma; }
    public Button pulsanteAnnulla() { return pulsanteAnnulla; }
}
