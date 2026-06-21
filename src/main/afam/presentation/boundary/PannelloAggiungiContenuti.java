package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PannelloAggiungiContenuti {
    private final Parent root;
    private final Button pulsanteAggiungiDocumento;
    private final Button pulsanteAggiungiAudio;
    private final Button pulsanteAggiungiFoto;
    private final Button pulsanteAggiungiVideo;
    private final Button pulsanteAnnulla;

    public PannelloAggiungiContenuti(UiFactory ui) {
        Label titolo = new Label("Aggiungi contenuti");
        titolo.getStyleClass().add("popup-title");
        Label descrizione = new Label("Seleziona il tipo di contenuto da caricare.");
        descrizione.setWrapText(true);
        descrizione.setMaxWidth(Double.MAX_VALUE);
        this.pulsanteAggiungiDocumento = ui.button("Aggiungi Documento", "primary");
        this.pulsanteAggiungiAudio = ui.button("Aggiungi Audio", "primary");
        this.pulsanteAggiungiFoto = ui.button("Aggiungi Foto", "primary");
        this.pulsanteAggiungiVideo = ui.button("Aggiungi Video", "primary");
        this.pulsanteAnnulla = ui.button("Annulla", "secondary");
        HBox boxAnnulla = new HBox(10, pulsanteAnnulla);
        boxAnnulla.setAlignment(javafx.geometry.Pos.CENTER);
        VBox panel = new VBox(14, titolo, descrizione,
                pulsanteAggiungiDocumento, pulsanteAggiungiAudio,
                pulsanteAggiungiFoto, pulsanteAggiungiVideo,
                boxAnnulla);
        panel.getStyleClass().add("popup-card");
        this.root = panel;
    }

    public Parent mostra() { return root; }
    public Button pulsanteAggiungiDocumento() { return pulsanteAggiungiDocumento; }
    public Button pulsanteAggiungiAudio() { return pulsanteAggiungiAudio; }
    public Button pulsanteAggiungiFoto() { return pulsanteAggiungiFoto; }
    public Button pulsanteAggiungiVideo() { return pulsanteAggiungiVideo; }
    public Button pulsanteAnnulla() { return pulsanteAnnulla; }
}
