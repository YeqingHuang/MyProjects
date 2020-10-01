package allday;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleLinkExample extends Application{

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Hyperlinks");
        stage.setWidth(500);
        stage.setHeight(200);
        VBox root = new VBox();

        //Hyperlinks
        Hyperlink link = new Hyperlink("https://www.deepl.com/en/translator");

        link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                HostServices host = getHostServices();
                host.showDocument(link.getText());
            }
        });

        root.getChildren().addAll(link);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}