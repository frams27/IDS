package afam.presentation.ui;

import afam.domain.entity.DatiCurriculari;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.awt.Desktop;
import java.nio.file.Files;
import java.nio.file.Path;

public class UiFactory {
    private final HostServices hostServices;
    private Runnable simulaPerditaConnessioneAction;

    public UiFactory(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setSimulaPerditaConnessioneAction(Runnable action) {
        this.simulaPerditaConnessioneAction = action;
    }

    public Parent page(String title, Runnable backAction, Node... content) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-root");
        root.setPrefSize(1100, 760);

        VBox panel = new VBox(14);
        panel.setPadding(new Insets(24));
        panel.setAlignment(Pos.CENTER);
        panel.setFillWidth(true);
        panel.setMinWidth(360);
        panel.setPrefWidth(760);
        panel.setMaxWidth(760);
        panel.getStyleClass().add("card");

        Label h1 = new Label(title);
        h1.getStyleClass().add("title");
        h1.setWrapText(true);
        h1.setMaxWidth(Double.MAX_VALUE);
        h1.setAlignment(Pos.CENTER);
        panel.getChildren().add(h1);

        for (Node node : content) normalizeContent(node);
        panel.getChildren().addAll(content);

        StackPane center = new StackPane(panel);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20));
        center.getStyleClass().add("screen-center");

        ScrollPane scroller = new ScrollPane(center);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroller.setPannable(true);
        scroller.getStyleClass().add("main-scroll");
        root.setCenter(scroller);

        boolean mostraPulsanteConnessione = simulaPerditaConnessioneAction != null
                && !"Nessuna connessione a Internet".equals(title);
        if (backAction != null || mostraPulsanteConnessione) {
            HBox top = new HBox(10);
            top.setPadding(new Insets(12, 0, 0, 12));
            top.setAlignment(Pos.TOP_LEFT);
            if (backAction != null) {
                Button back = button("← Torna indietro", "secondary");
                back.setOnAction(e -> backAction.run());
                top.getChildren().add(back);
            }
            if (mostraPulsanteConnessione) {
                Button simulaConnessione = button("Simula perdita connessione", "danger");
                simulaConnessione.setOnAction(e -> simulaPerditaConnessioneAction.run());
                top.getChildren().add(simulaConnessione);
            }
            root.setTop(top);
        }
        return root;
    }

    public Button button(String text, String styleClass) {
        Button b = new Button(text);
        if (styleClass != null && !styleClass.isBlank()) b.getStyleClass().add(styleClass);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    public TextField field(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }

    public PasswordField passwordField(String prompt) {
        PasswordField f = new PasswordField();
        f.setPromptText(prompt);
        f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }

    public TextArea limitedTextArea(String prompt, String value, int maxChars) {
        TextArea a = new TextArea(value == null ? "" : value);
        a.setPromptText(prompt);
        a.setWrapText(true);
        a.setPrefRowCount(4);
        a.textProperty().addListener((obs, oldV, newV) -> {
            if (newV != null && newV.length() > maxChars) {
                a.setText(newV.substring(0, maxChars));
                a.positionCaret(maxChars);
            }
        });
        return a;
    }

    public VBox datiCurriculariView(DatiCurriculari datiCurriculari) {
        VBox box = new VBox(10,
                sezioneDatiCurriculariSolaLettura("Biografia", datiCurriculari.biografia()),
                sezioneDatiCurriculariSolaLettura("Titoli di studio", datiCurriculari.titoliDiStudio()),
                sezioneDatiCurriculariSolaLettura("Esperienze artistiche e formative", datiCurriculari.esperienzeArtisticheEFormative())
        );
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(Double.MAX_VALUE);
        box.getStyleClass().add("curriculum-box");
        return box;
    }

    public void alert(Alert.AlertType type, String title, String message) {
        Alert a = new Alert(type);
        String readableTitle = title == null || title.isBlank() ? "Notifica" : title;
        a.setTitle(readableTitle);
        a.setHeaderText(readableTitle);
        a.setContentText(message);
        a.showAndWait();
    }

    public void openFile(Path path) {
        try {
            if (path == null || !Files.exists(path)) {
                alert(Alert.AlertType.ERROR, "File non trovato", "Il file non esiste più nel percorso registrato.");
                return;
            }
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(path.toFile());
            } else {
                hostServices.showDocument(path.toUri().toString());
            }
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Impossibile aprire il file", ex.getMessage());
        }
    }

    private VBox sezioneDatiCurriculariSolaLettura(String title, String value) {
        Label label = new Label(title);
        label.setStyle("-fx-font-weight: bold;");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

        TextArea area = new TextArea(defaultText(value));
        area.setWrapText(true);
        area.setEditable(false);
        area.setFocusTraversable(false);
        area.setPrefRowCount(3);
        area.setMinHeight(72);
        area.setMaxWidth(Double.MAX_VALUE);
        area.getStyleClass().add("readonly-area");

        VBox section = new VBox(4, label, area);
        section.setAlignment(Pos.CENTER);
        section.setMaxWidth(Double.MAX_VALUE);
        section.getStyleClass().add("curriculum-section");
        return section;
    }

    private void normalizeContent(Node node) {
        if (node == null) return;
        if (node instanceof Label label) {
            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
        }
        if (node instanceof Button button) button.setMaxWidth(Double.MAX_VALUE);
        if (node instanceof TextInputControl input) input.setMaxWidth(Double.MAX_VALUE);
        if (node instanceof DatePicker datePicker) datePicker.setMaxWidth(Double.MAX_VALUE);
        if (node instanceof ListView<?> listView) listView.setMaxWidth(Double.MAX_VALUE);
        if (node instanceof HBox hBox) {
            hBox.setAlignment(Pos.CENTER);
            hBox.setMaxWidth(Double.MAX_VALUE);
            for (Node child : hBox.getChildren()) normalizeContent(child);
        } else if (node instanceof VBox vBox) {
            vBox.setAlignment(Pos.CENTER);
            vBox.setMaxWidth(Double.MAX_VALUE);
            for (Node child : vBox.getChildren()) normalizeContent(child);
        } else if (node instanceof TilePane tilePane) {
            tilePane.setAlignment(Pos.CENTER);
            tilePane.setMaxWidth(Double.MAX_VALUE);
            tilePane.setPrefColumns(2);
            for (Node child : tilePane.getChildren()) normalizeContent(child);
        }
    }

    private static String defaultText(String s) {
        return s == null || s.isBlank() ? "Non compilato" : s;
    }
}
