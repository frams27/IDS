package afam.presentation.boundary;

import afam.domain.entity.LinkDiCondivisione;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class SchermataFeedbackContenuti {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneCondivisione;
    private Parent root;
    private ListView<LinkDiCondivisione> listaLink;
    private TextArea areaDettagli;
    private Button pulsanteVisualizzaFeedback;

    public static SchermataFeedbackContenuti create(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        return new SchermataFeedbackContenuti(ui, tornaAllaGestioneCondivisione);
    }

    public SchermataFeedbackContenuti(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        this.ui = ui;
        this.tornaAllaGestioneCondivisione = tornaAllaGestioneCondivisione;
    }

    public Parent mostra(ObservableList<LinkDiCondivisione> links) {
        this.listaLink = BoundarySupport.listaLink(links);
        this.areaDettagli = new TextArea();
        areaDettagli.setEditable(false);
        areaDettagli.setPrefRowCount(8);
        this.pulsanteVisualizzaFeedback = ui.button("Visualizza feedback", "primary");
        this.root = ui.page("Feedback contenuti", tornaAllaGestioneCondivisione, listaLink, pulsanteVisualizzaFeedback, areaDettagli);
        return root;
    }

    public Parent getRoot() { return root; }
    public ListView<LinkDiCondivisione> listaLink() { return listaLink; }
    public TextArea areaDettagli() { return areaDettagli; }
    public Button pulsanteVisualizzaFeedback() { return pulsanteVisualizzaFeedback; }
}
