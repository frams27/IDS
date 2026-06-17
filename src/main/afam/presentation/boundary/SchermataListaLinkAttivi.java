package afam.presentation.boundary;

import afam.domain.entity.LinkDiCondivisione;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class SchermataListaLinkAttivi {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneCondivisione;
    private Parent root;
    private ListView<LinkDiCondivisione> listaLinkAttivi;
    private Button pulsanteDisattiva;

    public static SchermataListaLinkAttivi create(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        return new SchermataListaLinkAttivi(ui, tornaAllaGestioneCondivisione);
    }

    public SchermataListaLinkAttivi(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        this.ui = ui;
        this.tornaAllaGestioneCondivisione = tornaAllaGestioneCondivisione;
    }

    public Parent mostra(ObservableList<LinkDiCondivisione> links) {
        this.listaLinkAttivi = BoundarySupport.listaLink(links);
        this.pulsanteDisattiva = ui.button("Disattiva", "danger");
        this.root = ui.page("Link attivi", tornaAllaGestioneCondivisione, listaLinkAttivi, pulsanteDisattiva);
        return root;
    }

    public Parent getRoot() { return root; }
    public ListView<LinkDiCondivisione> listaLinkAttivi() { return listaLinkAttivi; }
    public Button pulsanteDisattiva() { return pulsanteDisattiva; }
}
