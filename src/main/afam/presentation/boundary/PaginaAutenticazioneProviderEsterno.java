package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PaginaAutenticazioneProviderEsterno {
    private final Parent root;
    private final TextField campoEmailProvider;
    private final Button pulsanteEsitoPositivoProvider;
    private final Button pulsanteEsitoNegativoProvider;

    public static PaginaAutenticazioneProviderEsterno create(UiFactory ui, Runnable tornaAllaPaginaDiLogin) {
        return new PaginaAutenticazioneProviderEsterno(ui, tornaAllaPaginaDiLogin);
    }

    public PaginaAutenticazioneProviderEsterno(UiFactory ui, Runnable tornaAllaPaginaDiLogin) {
        this.campoEmailProvider = ui.field("Email account provider");
        this.pulsanteEsitoPositivoProvider = ui.button("Simula esito positivo provider", "primary");
        this.pulsanteEsitoNegativoProvider = ui.button("Simula esito negativo provider", "secondary");
        this.root = ui.page("Autenticazione con provider esterno", tornaAllaPaginaDiLogin,
                campoEmailProvider, pulsanteEsitoPositivoProvider, pulsanteEsitoNegativoProvider);
    }

    public Parent mostra() { return root; }
    public Parent getRoot() { return root; }
    public String emailProvider() { return campoEmailProvider.getText(); }
    public Button pulsanteEsitoPositivoProvider() { return pulsanteEsitoPositivoProvider; }
    public Button pulsanteEsitoNegativoProvider() { return pulsanteEsitoNegativoProvider; }
}
