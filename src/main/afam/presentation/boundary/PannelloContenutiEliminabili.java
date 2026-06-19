package afam.presentation.boundary;

import afam.domain.entity.ContenutoMultimediale;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PannelloContenutiEliminabili {
    private final UiFactory ui;
    private Parent root;
    private ListView<ContenutoMultimediale> listaContenutiEliminabili;
    private Button pulsanteConferma;
    private Button pulsanteAnnulla;

    public PannelloContenutiEliminabili(UiFactory ui) {
        this.ui = ui;
    }

    public Parent mostra(ObservableList<ContenutoMultimediale> contenuti) {
        Label titolo = new Label("Contenuti eliminabili");
        titolo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        this.listaContenutiEliminabili = ui.listaContenuti(contenuti);
        listaContenutiEliminabili.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listaContenutiEliminabili.setPrefHeight(200);
        this.pulsanteConferma = ui.button("Conferma", "danger");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        VBox panel = new VBox(14, titolo, listaContenutiEliminabili,
                new HBox(10, pulsanteConferma, pulsanteAnnulla));
        panel.getStyleClass().add("popup-card");
        this.root = panel;
        return root;
    }

    public ListView<ContenutoMultimediale> listaContenutiEliminabili() { return listaContenutiEliminabili; }
    public Button pulsanteConferma() { return pulsanteConferma; }
    public Button pulsanteAnnulla() { return pulsanteAnnulla; }
}
