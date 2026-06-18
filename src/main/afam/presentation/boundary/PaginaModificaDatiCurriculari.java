package afam.presentation.boundary;

import afam.domain.entity.DatiCurriculari;
import afam.presentation.ui.UiFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class PaginaModificaDatiCurriculari {
    private final UiFactory ui;
    private final Runnable tornaAllaGestioneProfilo;
    private Parent root;
    private TextArea campoBiografia;
    private TextArea campoTitoliDiStudio;
    private TextArea campoEsperienzeArtisticheEFormative;
    private Button pulsanteSalva;

    public PaginaModificaDatiCurriculari(UiFactory ui, Runnable tornaAllaGestioneProfilo) {
        this.ui = ui;
        this.tornaAllaGestioneProfilo = tornaAllaGestioneProfilo;
    }

    public Parent mostra(DatiCurriculari dati) {
        this.campoBiografia = ui.limitedTextArea("Biografia", dati.biografia(), 200);
        this.campoTitoliDiStudio = ui.limitedTextArea("Titoli di studio", dati.titoliDiStudio(), 200);
        this.campoEsperienzeArtisticheEFormative = ui.limitedTextArea("Esperienze artistiche e formative", dati.esperienzeArtisticheEFormative(), 200);
        this.pulsanteSalva = ui.button("Salva", "primary");
        this.root = ui.page("Modifica dati curriculari", tornaAllaGestioneProfilo,
                new Label("Biografia"), campoBiografia,
                new Label("Titoli di studio"), campoTitoliDiStudio,
                new Label("Esperienze artistiche e formative"), campoEsperienzeArtisticheEFormative,
                pulsanteSalva);
        return root;
    }

    public String biografia() { return campoBiografia.getText(); }
    public String titoliDiStudio() { return campoTitoliDiStudio.getText(); }
    public String esperienzeArtisticheEFormative() { return campoEsperienzeArtisticheEFormative.getText(); }
    public Button pulsanteSalva() { return pulsanteSalva; }
}
