package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PannelloDiNotifica {
    private final Parent root;
    private final Button pulsanteOk;

    public PannelloDiNotifica(UiFactory ui, Alert.AlertType tipo, String messaggio) {
        Label testoMessaggio = new Label(messaggio);
        testoMessaggio.setWrapText(true);
        testoMessaggio.setMaxWidth(Double.MAX_VALUE);
        this.pulsanteOk = ui.button("OK", stilePulsante(tipo));
        this.root = ui.page(titolo(tipo), null, testoMessaggio, pulsanteOk);
    }

    public Parent mostra() {
        return root;
    }


    public Button pulsanteOk() {
        return pulsanteOk;
    }

    private String titolo(Alert.AlertType tipo) {
        return switch (tipo) {
            case ERROR -> "Errore";
            case WARNING -> "Attenzione";
            case INFORMATION -> "Notifica";
            case CONFIRMATION -> "Conferma";
            default -> "Notifica";
        };
    }

    private String stilePulsante(Alert.AlertType tipo) {
        return switch (tipo) {
            case ERROR, WARNING -> "danger";
            default -> "primary";
        };
    }
}
