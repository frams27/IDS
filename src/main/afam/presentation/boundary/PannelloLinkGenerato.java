package afam.presentation.boundary;

import afam.domain.entity.LinkDiCondivisione;
import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PannelloLinkGenerato {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneCondivisione;
    private Parent root;
    private TextField campoLinkGenerato;
    private Button pulsanteCopiaLink;

    public static PannelloLinkGenerato create(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        return new PannelloLinkGenerato(ui, tornaAllaGestioneCondivisione);
    }

    public PannelloLinkGenerato(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        this.ui = ui;
        this.tornaAllaGestioneCondivisione = tornaAllaGestioneCondivisione;
    }

    public Parent mostra(LinkDiCondivisione link) {
        this.campoLinkGenerato = ui.field("Link generato");
        campoLinkGenerato.setText(link.url());
        campoLinkGenerato.setEditable(false);
        this.pulsanteCopiaLink = ui.button("Copia link", "primary");
        this.root = ui.page("Link generato", tornaAllaGestioneCondivisione, campoLinkGenerato, pulsanteCopiaLink);
        return root;
    }

    public Parent getRoot() { return root; }
    public String linkGenerato() { return campoLinkGenerato.getText(); }
    public Button pulsanteCopiaLink() { return pulsanteCopiaLink; }
}
