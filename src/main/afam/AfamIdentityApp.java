package afam;

import afam.domain.provider.BoundaryProviderEsterno;
import afam.domain.repository.BoundaryDBMS;
import afam.infrastructure.provider.ProviderEsternoSimulato;
import afam.infrastructure.repository.SQLiteBoundaryDBMS;
import afam.presentation.navigation.AppNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

/** Entry point dell'applicazione: assembla l'infrastructure e avvia il Presentation Tier. */
public class AfamIdentityApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BoundaryDBMS boundaryDBMS = new SQLiteBoundaryDBMS();
        BoundaryProviderEsterno boundaryProviderEsterno = new ProviderEsternoSimulato();
        new AppNavigator(getHostServices(), boundaryDBMS, boundaryProviderEsterno).start(stage);
    }
}
