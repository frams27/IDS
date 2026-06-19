package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PannelloAccessoLink {
    private final Parent root;
    private final TextField campoLink;
    private final Button pulsanteConferma;
    private final Button pulsanteAnnulla;

    public PannelloAccessoLink(UiFactory ui) {
        Label titolo = new Label("Accesso tramite link");
        titolo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        this.campoLink = ui.field("Inserisci il link di condivisione");
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        VBox panel = new VBox(14, titolo, campoLink,
                new HBox(10, pulsanteConferma, pulsanteAnnulla));
        panel.getStyleClass().add("popup-card");
        this.root = panel;
    }

    public Parent mostra() { return root; }
    public String linkInserito() { return campoLink.getText(); }
    public Button pulsanteConferma() { return pulsanteConferma; }
    public Button pulsanteAnnulla() { return pulsanteAnnulla; }
}
