package app;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Scene1 {
    VBox vBox;  // holds label and goButton
    Label label;
    Button goButton;
    HostServices hostServices;

    Scene2 scene2;  // it needs scene2 to setOnAction
    Scene currentScene; // i.e itself

    public Scene1(Stage stage, HostServices hostServices) throws FileNotFoundException {

        this.hostServices = hostServices;
        // create a message label
        label = new Label("Welcome to the movie library!");
        label.setFont(new Font("Arial",20));
        label.setTextFill(Color.web("#F0F8FF"));

        // create a goButton
        goButton = new Button("Go to search");
        goButton.setMinWidth(40);
        goButton.setOnAction(event -> {
            try {
                if (scene2 == null) {
                    scene2 = new Scene2(stage, this);
                }
                stage.setScene(scene2.getScene());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        // create a Vbox to hold these two nodes
        vBox = new VBox(20);
        vBox.getChildren().addAll(label,goButton);
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.setAlignment(Pos.CENTER);

        // create a background image for the VBox
        FileInputStream input = new FileInputStream("./spaceman.png");
        Image image = new Image(input);

        // let the image fit in with the window size
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background background = new Background(backgroundimage);
        vBox.setBackground(background);
    }

    public Scene getScene(){
        if (currentScene == null) {
            currentScene = new Scene(vBox,680,380);
        }
        return currentScene;
    }
}