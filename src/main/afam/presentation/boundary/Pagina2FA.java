package afam.presentation.boundary;

import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Pagina2FA {
    private final Parent root;
    private final TextField campoCodiceVerifica;
    private final Button pulsanteGeneraOTP;
    private final Button pulsanteVerificaOTP;

    public static Pagina2FA create(UiFactory ui, Runnable tornaAllaPaginaDiLogin) {
        return new Pagina2FA(ui, tornaAllaPaginaDiLogin);
    }

    public Pagina2FA(UiFactory ui, Runnable tornaAllaPaginaDiLogin) {
        this.campoCodiceVerifica = ui.field("Codice di verifica");
        this.pulsanteGeneraOTP = ui.button("Genera OTP", "secondary");
        this.pulsanteVerificaOTP = ui.button("Verifica OTP", "primary");
        this.root = ui.page("Pagina 2FA", tornaAllaPaginaDiLogin,
                new HBox(10, pulsanteGeneraOTP, pulsanteVerificaOTP), campoCodiceVerifica);
    }

    public Parent mostra() { return root; }
    public Parent getRoot() { return root; }
    public String codiceVerifica() { return campoCodiceVerifica.getText(); }
    public Button pulsanteGeneraOTP() { return pulsanteGeneraOTP; }
    public Button pulsanteVerificaOTP() { return pulsanteVerificaOTP; }
}
