package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PaginaContenutiVisualizzabili {
    private final Parent root;
    private final Button pulsanteSelezionaContenuti;

    public PaginaContenutiVisualizzabili(UiFactory ui, Runnable tornaAllaGestioneCondivisione) {
        this.pulsanteSelezionaContenuti = ui.button("Seleziona contenuti", "primary");
        this.root = ui.page("Contenuti visualizzabili", tornaAllaGestioneCondivisione,
                new Label("Clicca sul tasto per selezionare i contenuti visibili dal link."),
                pulsanteSelezionaContenuti);
    }

    public Parent mostra() { return root; }
    public Button pulsanteSelezionaContenuti() { return pulsanteSelezionaContenuti; }
}
