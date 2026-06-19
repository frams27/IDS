package afam.presentation.boundary;

import afam.domain.entity.ContenutoMultimediale;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PannelloContenutiVisualizzabili {
    private final UiFactory ui;
    private Parent root;
    private final Map<ContenutoMultimediale, CheckBox> checkMap = new LinkedHashMap<>();
    private Button pulsanteConferma;
    private Button pulsanteAnnulla;

    public PannelloContenutiVisualizzabili(UiFactory ui) {
        this.ui = ui;
    }

    public Parent mostra(ObservableList<ContenutoMultimediale> contenuti) {
        Label titolo = new Label("Seleziona contenuti");
        titolo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label descrizione = new Label("Seleziona uno o più contenuti.");
        descrizione.setWrapText(true);
        descrizione.setMaxWidth(Double.MAX_VALUE);
        checkMap.clear();
        VBox checks = new VBox(8);
        for (ContenutoMultimediale item : contenuti) {
            CheckBox cb = new CheckBox(item.titolo() + " - " + item.formato());
            checkMap.put(item, cb);
            checks.getChildren().add(cb);
        }
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        VBox panel = new VBox(14, titolo, descrizione, checks,
                new HBox(10, pulsanteConferma, pulsanteAnnulla));
        panel.getStyleClass().add("popup-card");
        this.root = panel;
        return root;
    }

    public List<ContenutoMultimediale> contenutiSelezionati() {
        return checkMap.entrySet().stream()
                .filter(en -> en.getValue().isSelected())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    public Button pulsanteConferma() { return pulsanteConferma; }
    public Button pulsanteAnnulla() { return pulsanteAnnulla; }
}
