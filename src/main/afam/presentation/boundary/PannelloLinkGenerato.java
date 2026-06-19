package afam.presentation.boundary;

import afam.domain.entity.LinkDiCondivisione;
import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PannelloLinkGenerato {
    private final UiFactory ui;
    private Parent root;
    private TextField campoLinkGenerato;
    private Button pulsanteCopiaLink;

    public PannelloLinkGenerato(UiFactory ui) {
        this.ui = ui;
    }

    public Parent mostra(LinkDiCondivisione link) {
        Label titolo = new Label("Link generato");
        titolo.getStyleClass().add("popup-title");
        this.campoLinkGenerato = ui.field("Link generato");
        campoLinkGenerato.setText(link.url());
        campoLinkGenerato.setEditable(false);
        this.pulsanteCopiaLink = ui.button("Copia Link", "primary");
        VBox panel = new VBox(14, titolo, campoLinkGenerato, pulsanteCopiaLink);
        panel.getStyleClass().add("popup-card");
        this.root = panel;
        return root;
    }

    public String linkGenerato() { return campoLinkGenerato.getText(); }
    public Button pulsanteCopiaLink() { return pulsanteCopiaLink; }
}
