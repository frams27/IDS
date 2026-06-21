package afam.presentation.boundary;

import afam.domain.entity.LinkDiCondivisione;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class PaginaLinkAttivi {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneCondivisione;
    private Parent root;
    private ListView<LinkDiCondivisione> listaLinkAttivi;
    private Button pulsanteElimina;

    public PaginaLinkAttivi(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        this.ui = ui;
        this.tornaAllaGestioneCondivisione = tornaAllaGestioneCondivisione;
    }

    public Parent mostra(ObservableList<LinkDiCondivisione> links) {
        this.listaLinkAttivi = ui.listaLink(links);
        this.pulsanteElimina = ui.button("Disattiva", "danger");
        this.root = ui.page("Link attivi", tornaAllaGestioneCondivisione, listaLinkAttivi, pulsanteElimina);
        return root;
    }

    public ListView<LinkDiCondivisione> listaLinkAttivi() { return listaLinkAttivi; }
    public Button pulsanteElimina() { return pulsanteElimina; }
}
