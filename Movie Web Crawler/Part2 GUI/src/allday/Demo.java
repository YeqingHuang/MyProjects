package allday;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Demo extends Application {

    private final TableView<Person> table = new TableView<>();
    private final ObservableList<Person> data = FXCollections.observableArrayList(
            new Person("Jacob", "Smith", "https://www.deepl.com/en/translator"),
            new Person("Isabella", "Johnson", "https://www.google.com/maps"),
            new Person("Ethan", "Williams", "https://www.google.com/maps")
    );

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setWidth(450);
        stage.setHeight(500);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));


        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn actionCol = new TableColumn("Website");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("link"));
        actionCol.setCellFactory(new HyperlinkPersonCell());
        HyperlinkPersonCell.setHostServices(getHostServices()); // IMPORTANT

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, actionCol);

        BorderPane borderPane = new BorderPane(table);
        Scene scene = new Scene(borderPane, 1000, 600);

        stage.setScene(scene);
        stage.show();
    }
}