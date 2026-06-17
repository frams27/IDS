package afam.presentation.boundary;

import afam.application.dto.PortfolioCondiviso;
import afam.domain.entity.ContenutoMultimediale;
import afam.presentation.ui.UiFactory;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;

public class SchermataContenutiCondivisi {
    private final UiFactory ui;
    private final Runnable tornaAllaStartingPage;
    private Parent root;
    private ListView<ContenutoMultimediale> listaContenutiMultimediali;
    private Button pulsanteTornaAllaSchermataIniziale;

    public static SchermataContenutiCondivisi create(UiFactory ui, Runnable tornaAllaStartingPage) {
        return new SchermataContenutiCondivisi(ui, tornaAllaStartingPage);
    }

    public SchermataContenutiCondivisi(UiFactory ui, Runnable tornaAllaStartingPage) {
        this.ui = ui;
        this.tornaAllaStartingPage = tornaAllaStartingPage;
    }

    public Parent mostra(PortfolioCondiviso portfolio) {
        this.listaContenutiMultimediali = BoundarySupport.listaContenuti(FXCollections.observableArrayList(portfolio.contenutiMultimediali()));
        listaContenutiMultimediali.setPrefHeight(330);
        this.pulsanteTornaAllaSchermataIniziale = ui.button("Torna alla schermata iniziale", "secondary");
        this.root = ui.page("Contenuti condivisi", tornaAllaStartingPage,
                new Label("Dati curriculari"), ui.datiCurriculariView(portfolio.datiCurriculari()),
                new Separator(), new Label("Contenuti multimediali"), listaContenutiMultimediali,
                pulsanteTornaAllaSchermataIniziale);
        return root;
    }

    public Parent getRoot() { return root; }
    public ListView<ContenutoMultimediale> listaContenutiMultimediali() { return listaContenutiMultimediali; }
    public Button pulsanteTornaAllaSchermataIniziale() { return pulsanteTornaAllaSchermataIniziale; }
}
