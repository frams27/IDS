package afam.presentation.boundary;

import afam.domain.entity.ContenutoMultimediale;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class PannelloContenutiEliminabili {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneProfilo;
    private Parent root;
    private ListView<ContenutoMultimediale> listaContenutiEliminabili;
    private Button pulsanteConferma;

    public static PannelloContenutiEliminabili create(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        return new PannelloContenutiEliminabili(ui, tornaAllaGestioneProfilo);
    }

    public PannelloContenutiEliminabili(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        this.ui = ui;
        this.tornaAllaGestioneProfilo = tornaAllaGestioneProfilo;
    }

    public Parent mostra(ObservableList<ContenutoMultimediale> contenuti) {
        this.listaContenutiEliminabili = BoundarySupport.listaContenuti(contenuti);
        listaContenutiEliminabili.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.pulsanteConferma = ui.button("Conferma", "danger");
        this.root = ui.page("Contenuti eliminabili", tornaAllaGestioneProfilo, listaContenutiEliminabili, pulsanteConferma);
        return root;
    }

    public Parent getRoot() { return root; }
    public ListView<ContenutoMultimediale> listaContenutiEliminabili() { return listaContenutiEliminabili; }
    public Button pulsanteConferma() { return pulsanteConferma; }
}
