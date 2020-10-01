package app;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import model.Model;
import model.MyMovie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class OldMain extends Application {

    Scene scene1, scene2;

    public OldMain() throws FileNotFoundException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Stage window = primaryStage;
        window.setTitle("Movie Library");

        // TODO: below code is for scene1
        // create a message label
        Label label = new Label("Welcome to the movie library!");
        label.setFont(new Font("Arial",20));
        label.setTextFill(Color.web("#F0F8FF"));

        // create a goButton
        Button goButton = new Button("Go to search");
        goButton.setMinWidth(40);
        goButton.setOnAction(event -> window.setScene(scene2));

        // create a Vbox to hold these two nodes
        VBox vBox = new VBox(20);
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

        scene1 = new Scene(vBox,680,380);

        // TODO: below code is for scene2
        TableView<MyMovie> table = new TableView<>();
        table.setStyle("-fx-selection-bar: lightgrey");
        // use a helper function column() to create each TableColumn for this table
        table.getColumns().addAll(column("Title(ENG)", MyMovie::ITitleProperty,200),
                column("Title(CHN)",MyMovie::DTitleProperty,150),
                column("Year", MyMovie::yearProperty,60),
                column("IMDbRank",MyMovie::imdbRankProperty,50),
                column("IMDbViewers", MyMovie::imdbViewerProperty,50),
                column("DoubanRank",MyMovie::doubanRankProperty,50),
                column("DoubanViewers",MyMovie::doubanViewerProperty,50),
                column("Genres", MyMovie::genresProperty,200),
                column("Region", MyMovie::countryProperty,150));

        TableColumn<MyMovie,String> col1 = new TableColumn<>("Link1");
        col1.setCellValueFactory(new PropertyValueFactory("ILink"));
        col1.setMinWidth(250);
        col1.setCellFactory(new HyperlinkCell());

        TableColumn<MyMovie,String> col2 = new TableColumn<MyMovie, String>("Link2");
        col2.setCellValueFactory(new PropertyValueFactory<>("DLink"));
        col2.setCellFactory(new HyperlinkCell());
        col2.setMinWidth(300);

        HyperlinkCell.setHostServices(getHostServices()); // IMPORTANT
        table.getColumns().addAll(col1, col2);

        // Note that FilteredList loses the built-in sorting property.
        // We must create both a SortedList and a FilteredList to make the table sortable.
        List<MyMovie> rawData = Model.getData("./final.txt");
        ObservableList<MyMovie> movieList = FXCollections.observableList(rawData);
        FilteredList<MyMovie> filteredList = new FilteredList(movieList);
        SortedList<MyMovie> sortedList = new SortedList<>(filteredList);
        table.setItems(sortedList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());

        // TODO set a titleFilterField properly
        TextField titleFilterFiled = new TextField();
        titleFilterFiled.setPromptText("Type in a title");

        ObjectProperty<Predicate<MyMovie>> titleFilter = new SimpleObjectProperty<>();
        titleFilter.bind(Bindings.createObjectBinding(() -> myMovie
                        -> myMovie.getITitle().toLowerCase().contains(titleFilterFiled.getText().toLowerCase()),
                titleFilterFiled.textProperty()));

        // TODO create 3 choice boxes to interact with Filters
        ChoiceBox<String> countryBox = new ChoiceBox<>();
        countryBox.getItems().addAll("All Regions","USA", "UK", "Germany","France","Italy",
                "China","India","Japan","South Korea");
        countryBox.setValue("All Regions");

        ChoiceBox<String> genresBox = new ChoiceBox<>();
        genresBox.getItems().addAll("All Genres","Drama","Action","Adventure","Animation",
                "Sci-Fi", "Romance","Comedy","War","Crime","Thriller");
        genresBox.setValue("All Genres");

        ChoiceBox<String> yearBox = new ChoiceBox<>();
        yearBox.getItems().addAll("All Years", "Before 1960","1961-1970","1971-1980",
                "1981-1990", "1991-2000", "2001-2010","2010-2020");
        yearBox.setValue("All Years");

        // TODO associate the choice boxes with filters
        ObjectProperty<Predicate<MyMovie>> countryFilter = new SimpleObjectProperty<>();
        countryFilter.bind(Bindings.createObjectBinding(() -> myMovie ->
                        myMovie.getCountry().contains(countryBox.getValue().equals("All Regions")? "":countryBox.getValue()),
                countryBox.valueProperty()));

        ObjectProperty<Predicate<MyMovie>> genresFilter = new SimpleObjectProperty<>();
        genresFilter.bind(Bindings.createObjectBinding(() -> myMovie ->
                        myMovie.getGenres().contains(genresBox.getValue().equals("All Genres")? "":genresBox.getValue()),
                genresBox.valueProperty()));

        ObjectProperty<Predicate<MyMovie>> yearFilter = new SimpleObjectProperty<>();
        yearFilter.bind(Bindings.createObjectBinding(() -> {
            String period = yearBox.getValue();
            int start,end;
            if (period.equals("All Years")){
                start = 1900;
                end = 2020;
            }
            else if (period.equals("Before 1960")){
                start = 1900;
                end = 1960;
            }
            else {
                start = Integer.parseInt(period.substring(0,4));
                end = Integer.parseInt(period.substring(5,9));
            }
            return myMovie -> myMovie.getYear() >= start && myMovie.getYear() <= end;
        },yearBox.valueProperty()));

        // TODO apply these filters to the filteredList
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(
                () -> countryFilter.get().and(genresFilter.get()).and(yearFilter.get()).and(titleFilter.get()),
                countryFilter,genresFilter,yearFilter,titleFilter));


        Button clearButton = new Button("Clear Filters");
        clearButton.setOnAction(actionEvent -> {
            titleFilterFiled.clear();
            countryBox.setValue("All Regions");
            genresBox.setValue("All Genres");
            yearBox.setValue("All Years");
        });

        Button exportButton = new Button("Export");
        exportButton.setOnAction(actionEvent -> {
            try {
                ExportFile.writeExcel(filteredList,primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> window.setScene(scene1));

        HBox topSection = new HBox(5, titleFilterFiled, genresBox, countryBox, yearBox,
                clearButton,exportButton, backButton);
        topSection.setPadding(new Insets(10,5,10,5));
        BorderPane borderPane = new BorderPane(table, topSection, null, null, null);
        scene2 = new Scene(borderPane, 1000, 600);

        // let scene1 display first
        window.setScene(scene1);
        window.show();
    }


    private static <S,T> TableColumn<S,T> column(String title, Function<S, ObservableValue<T>> property, int width) {
        TableColumn<S,T> col = new TableColumn<>(title);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        col.setMinWidth(width);

        // Override the CellFactory for quantity to leave the cell empty if value is 251
        col.setCellFactory(param -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T rank, boolean empty) {
                super.updateItem(rank, empty);
                if (empty || rank.equals(251)) {
                    setText(null);
                } else {
                    setText(rank.toString());
                }
            }
        });
        return col ;
    }

    public static void main(String args[]) throws Exception {
        // Launch the application
        launch(args);
    }
}