package afam.presentation.boundary;

import afam.domain.entity.ContenutoMultimediale;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class PaginaOrganizzaContenuti {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneProfilo;
    private Parent root;
    private ListView<ContenutoMultimediale> listaContenuti;
    private Button pulsanteSpostaSu;
    private Button pulsanteSpostaGiu;
    private Button pulsanteConferma;

    public static PaginaOrganizzaContenuti create(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        return new PaginaOrganizzaContenuti(ui, tornaAllaGestioneProfilo);
    }

    public PaginaOrganizzaContenuti(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        this.ui = ui;
        this.tornaAllaGestioneProfilo = tornaAllaGestioneProfilo;
    }

    public Parent mostra(ObservableList<ContenutoMultimediale> contenuti) {
        this.listaContenuti = BoundarySupport.listaContenuti(contenuti);
        listaContenuti.setPrefHeight(360);
        this.pulsanteSpostaSu = ui.button("Sposta su", "secondary");
        this.pulsanteSpostaGiu = ui.button("Sposta giù", "secondary");
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.root = ui.page("Organizza contenuti", tornaAllaGestioneProfilo,
                listaContenuti, new HBox(10, pulsanteSpostaSu, pulsanteSpostaGiu, pulsanteConferma));
        return root;
    }

    public Parent getRoot() { return root; }
    public ListView<ContenutoMultimediale> listaContenuti() { return listaContenuti; }
    public Button pulsanteSpostaSu() { return pulsanteSpostaSu; }
    public Button pulsanteSpostaGiu() { return pulsanteSpostaGiu; }
    public Button pulsanteConferma() { return pulsanteConferma; }
}
