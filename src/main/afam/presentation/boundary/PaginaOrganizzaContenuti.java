package afam.presentation.boundary;

import afam.domain.entity.ContenutoMultimediale;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.Collections;

public class PaginaOrganizzaContenuti {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneProfilo;
    private Parent root;
    private ListView<ContenutoMultimediale> listaContenuti;
    private Button pulsanteSpostaSu;
    private Button pulsanteSpostaGiu;
    private Button pulsanteConferma;
    private Button pulsanteAnnulla;

    public PaginaOrganizzaContenuti(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        this.ui = ui;
        this.tornaAllaGestioneProfilo = tornaAllaGestioneProfilo;
    }

    public Parent mostra(ObservableList<ContenutoMultimediale> contenuti) {
        this.listaContenuti = ui.listaContenuti(contenuti);
        listaContenuti.setPrefHeight(360);
        this.pulsanteSpostaSu = ui.button("Sposta su", "secondary");
        this.pulsanteSpostaGiu = ui.button("Sposta giù", "secondary");
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        this.root = ui.page("Organizza contenuti", tornaAllaGestioneProfilo,
                listaContenuti, new HBox(10, pulsanteSpostaSu, pulsanteSpostaGiu, pulsanteConferma, pulsanteAnnulla));
        return root;
    }

    public void spostaSelezionato(int delta) {
        int idx = listaContenuti.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;
        int target = idx + delta;
        if (target < 0 || target >= listaContenuti.getItems().size()) return;
        Collections.swap(listaContenuti.getItems(), idx, target);
        listaContenuti.getSelectionModel().select(target);
    }

    public ListView<ContenutoMultimediale> listaContenuti() { return listaContenuti; }
    public Button pulsanteSpostaSu() { return pulsanteSpostaSu; }
    public Button pulsanteSpostaGiu() { return pulsanteSpostaGiu; }
    public Button pulsanteConferma() { return pulsanteConferma; }
    public Button pulsanteAnnulla() { return pulsanteAnnulla; }
}
