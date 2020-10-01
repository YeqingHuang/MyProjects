package app;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        HostServices hostServices = getHostServices();
        Scene1 welcomeScene = new Scene1(primaryStage, hostServices);
        primaryStage.setScene(welcomeScene.getScene());

        primaryStage.setTitle("Movie Library");

        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the application
        launch(args);
    }
}