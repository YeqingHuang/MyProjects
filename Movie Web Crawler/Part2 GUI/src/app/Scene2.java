package app;

import model.*;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Scene2 {
    BorderPane borderPane; // holds table and topSection
    TableView<MyMovie> table;
    HBox topSection;

    Scene1 scene1;
    Scene currentScene;

    public Scene2(Stage stage, Scene1 scene1) throws FileNotFoundException {

        this.scene1 = scene1;

        table = new TableView<>();
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
                column("Region", MyMovie::countryProperty,180));

        TableColumn<MyMovie,String> col1 = new TableColumn<>("Link1");
        col1.setCellValueFactory(new PropertyValueFactory("ILink"));
        col1.setMinWidth(250);
        col1.setCellFactory(new HyperlinkCell());

        TableColumn<MyMovie,String> col2 = new TableColumn<MyMovie, String>("Link2");
        col2.setCellValueFactory(new PropertyValueFactory<>("DLink"));
        col2.setCellFactory(new HyperlinkCell());
        col2.setMinWidth(220);

        HyperlinkCell.setHostServices(scene1.hostServices); // IMPORTANT
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
        TextField filterFiled = new TextField();
        filterFiled.setPromptText("Type in a title...");

        ObjectProperty<Predicate<MyMovie>> titleFilter = new SimpleObjectProperty<>();
        titleFilter.bind(Bindings.createObjectBinding(() -> myMovie
                        -> myMovie.getITitle().toLowerCase().contains(filterFiled.getText().toLowerCase()),
                filterFiled.textProperty()));

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
        yearBox.getItems().addAll("All Years", "1920-1960","1961-1970","1971-1980",
                "1981-1990", "1991-2000", "2001-2010","2011-2020");
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
            int start,end;
            String period = yearBox.getValue();
            if (period.equals("All Years")){
                start = 1920;
                end = 2020;
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
            filterFiled.clear();
            countryBox.setValue("All Regions");
            genresBox.setValue("All Genres");
            yearBox.setValue("All Years");
        });

        Button exportButton = new Button("Export");
        exportButton.setOnAction(actionEvent -> {
            try {
                ExportFile.writeExcel(filteredList,stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> stage.setScene(scene1.getScene()));

        topSection = new HBox(5, filterFiled, genresBox, countryBox, yearBox,
                clearButton, exportButton, backButton);
        topSection.setPadding(new Insets(10,5,10,5));

        borderPane = new BorderPane(table, topSection, null, null, null);
    }

    private static <S,T> TableColumn<S,T> column(String title, Function<S, ObservableValue<T>> property, int width) {
        TableColumn<S,T> col = new TableColumn<>(title);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        col.setMinWidth(width);

        // Override the CellFactory for quantity to leave the cell empty if value is 251
        col.setCellFactory(param -> new TableCell<>() {
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

    public Scene getScene(){
        if (currentScene == null) {
            currentScene = new Scene(borderPane, 1000, 600);
        }
        return currentScene;
    }
}
