package afam;

import afam.presentation.navigation.AppNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

/** Entry point del Presentation Tier. */
public class AfamIdentityApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        new AppNavigator(getHostServices()).start(stage);
    }
}
