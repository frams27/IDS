package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PaginaRecuperaPassword {
    private final Parent root;
    private final TextField campoEmail;
    private final Button pulsanteInvia;

    public PaginaRecuperaPassword(UiFactory ui, Runnable tornaAllaPaginaDiLogin) {
        this.campoEmail = ui.field("Email");
        this.pulsanteInvia = ui.button("Invia", "primary");
        this.root = ui.page("Pagina recupera password", tornaAllaPaginaDiLogin,
                new Label("Email"), campoEmail, pulsanteInvia);
    }

    public Parent mostra() { return root; }
    public String email() { return campoEmail.getText(); }
    public Button pulsanteInvia() { return pulsanteInvia; }
}
