package afam.presentation.boundary;

import afam.domain.entity.LinkDiCondivisione;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class PaginaFeedbackContenuti {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneCondivisione;
    private Parent root;
    private ListView<LinkDiCondivisione> listaLink;
    public PaginaFeedbackContenuti(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        this.ui = ui;
        this.tornaAllaGestioneCondivisione = tornaAllaGestioneCondivisione;
    }

    public Parent mostra(ObservableList<LinkDiCondivisione> links) {
        this.listaLink = ui.listaLink(links);
        Button pulsanteTornaIndietro = ui.button("Torna indietro", "secondary");
        pulsanteTornaIndietro.setOnAction(e -> tornaAllaGestioneCondivisione.run());
        this.root = ui.page("Feedback contenuti", tornaAllaGestioneCondivisione,
                listaLink, new HBox(10, pulsanteTornaIndietro));
        return root;
    }

    public ListView<LinkDiCondivisione> listaLink() { return listaLink; }
}
