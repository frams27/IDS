package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PannelloAggiungiContenuti {
    private final Parent root;
    private final Button pulsanteAggiungiDocumento;
    private final Button pulsanteAggiungiAudio;
    private final Button pulsanteAggiungiFoto;
    private final Button pulsanteAggiungiVideo;

    public PannelloAggiungiContenuti(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        this.pulsanteAggiungiDocumento = ui.button("Aggiungi documento", "primary");
        this.pulsanteAggiungiAudio = ui.button("Aggiungi audio", "primary");
        this.pulsanteAggiungiFoto = ui.button("Aggiungi foto", "primary");
        this.pulsanteAggiungiVideo = ui.button("Aggiungi video", "primary");
        this.root = ui.page("Aggiungi contenuti", tornaAllaGestioneProfilo,
                new Label("Seleziona il tipo di contenuto da caricare."),
                pulsanteAggiungiDocumento,
                pulsanteAggiungiAudio,
                pulsanteAggiungiFoto,
                pulsanteAggiungiVideo);
    }

    public Parent mostra() { return root; }
    public Button pulsanteAggiungiDocumento() { return pulsanteAggiungiDocumento; }
    public Button pulsanteAggiungiAudio() { return pulsanteAggiungiAudio; }
    public Button pulsanteAggiungiFoto() { return pulsanteAggiungiFoto; }
    public Button pulsanteAggiungiVideo() { return pulsanteAggiungiVideo; }
}
