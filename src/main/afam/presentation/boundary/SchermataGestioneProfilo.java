package afam.presentation.boundary;

import afam.domain.entity.ContenutoMultimediale;
import afam.domain.entity.DatiCurriculari;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.TilePane;

public class SchermataGestioneProfilo {
    private final UiFactory ui;
    private final Runnable tornaAllaHomePage;
    private Parent root;
    private ListView<ContenutoMultimediale> listaContenuti;
    private Button pulsanteAggiungiContenuti;
    private Button pulsanteEliminaContenuti;
    private Button pulsanteOrganizzaContenuti;
    private Button pulsanteModificaDatiCurriculari;
    private Button pulsanteModificaPassword;

    public static SchermataGestioneProfilo create(UiFactory ui, Runnable tornaAllaHomePage) {
        return new SchermataGestioneProfilo(ui, tornaAllaHomePage);
    }

    public SchermataGestioneProfilo(UiFactory ui, Runnable tornaAllaHomePage) {
        this.ui = ui;
        this.tornaAllaHomePage = tornaAllaHomePage;
    }

    public Parent mostra(DatiCurriculari dati, ObservableList<ContenutoMultimediale> contenuti) {
        this.listaContenuti = BoundarySupport.listaContenuti(contenuti);
        listaContenuti.setPrefHeight(250);
        this.pulsanteAggiungiContenuti = ui.button("Aggiungi contenuti", "primary");
        this.pulsanteEliminaContenuti = ui.button("Elimina contenuti", "danger");
        this.pulsanteOrganizzaContenuti = ui.button("Organizza contenuti", "secondary");
        this.pulsanteModificaDatiCurriculari = ui.button("Modifica dati curriculari", "secondary");
        this.pulsanteModificaPassword = ui.button("Modifica password", "secondary");
        TilePane actions = new TilePane(10, 10, pulsanteAggiungiContenuti, pulsanteEliminaContenuti, pulsanteOrganizzaContenuti,
                pulsanteModificaDatiCurriculari, pulsanteModificaPassword);
        this.root = ui.page("Gestione profilo", tornaAllaHomePage,
                new Label("Dati curriculari"), ui.datiCurriculariView(dati),
                new Separator(), new Label("Contenuti caricati"), listaContenuti,
                actions);
        return root;
    }

    public Parent getRoot() { return root; }
    public ListView<ContenutoMultimediale> listaContenuti() { return listaContenuti; }
    public Button pulsanteAggiungiContenuti() { return pulsanteAggiungiContenuti; }
    public Button pulsanteEliminaContenuti() { return pulsanteEliminaContenuti; }
    public Button pulsanteOrganizzaContenuti() { return pulsanteOrganizzaContenuti; }
    public Button pulsanteModificaDatiCurriculari() { return pulsanteModificaDatiCurriculari; }
    public Button pulsanteModificaPassword() { return pulsanteModificaPassword; }
}
