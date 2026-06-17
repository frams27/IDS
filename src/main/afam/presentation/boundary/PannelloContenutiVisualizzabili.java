package afam.presentation.boundary;

import afam.domain.entity.ContenutoMultimediale;
import afam.presentation.ui.UiFactory;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PannelloContenutiVisualizzabili {
    private final UiFactory ui;
    private final Runnable tornaAllaPaginaContenutiVisualizzabili;
    private Parent root;
    private final Map<ContenutoMultimediale, CheckBox> checkMap = new LinkedHashMap<>();
    private Button pulsanteConferma;

    public static PannelloContenutiVisualizzabili create(UiFactory ui, Runnable tornaAllaPaginaContenutiVisualizzabili) {
        return new PannelloContenutiVisualizzabili(ui, tornaAllaPaginaContenutiVisualizzabili);
    }

    public PannelloContenutiVisualizzabili(UiFactory ui, Runnable tornaAllaPaginaContenutiVisualizzabili) {
        this.ui = ui;
        this.tornaAllaPaginaContenutiVisualizzabili = tornaAllaPaginaContenutiVisualizzabili;
    }

    public Parent mostra(ObservableList<ContenutoMultimediale> contenuti) {
        checkMap.clear();
        VBox checks = new VBox(8);
        for (ContenutoMultimediale item : contenuti) {
            CheckBox cb = new CheckBox("[" + item.tipo() + "] " + item.titolo() + " - " + item.formato());
            checkMap.put(item, cb);
            checks.getChildren().add(cb);
        }
        this.pulsanteConferma = ui.button("Conferma", "primary");
        this.root = ui.page("Pannello contenuti visualizzabili", tornaAllaPaginaContenutiVisualizzabili,
                new Label("Seleziona uno o piu contenuti."), checks, pulsanteConferma);
        return root;
    }

    public Parent getRoot() { return root; }
    public List<ContenutoMultimediale> contenutiSelezionati() {
        return checkMap.entrySet().stream()
                .filter(en -> en.getValue().isSelected())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    public Button pulsanteConferma() { return pulsanteConferma; }
}
