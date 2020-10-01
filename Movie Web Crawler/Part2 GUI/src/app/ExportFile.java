package app;

import model.MyMovie;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class ExportFile {

    public static void writeExcel(List<MyMovie> movies, Stage stage) throws Exception {

        FileChooser fileChooser = new FileChooser();
        // restrict the user to export the data only in csv format
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        Writer writer = null;
        // in case the user changes his mind and click cancel, i.e. no file is created
        // we need to check if file is null
        if (file != null){
            try {
                writer = new BufferedWriter(new FileWriter(file));
                String titleRow = "Title(ENG),Title(CHN),Year,IMDbRank,IMDBviewers,DoubanRank,DoubanViewers," +
                        "Genres,Region,Link1,Link2\n";
                writer.write(titleRow);

                for (MyMovie movie:movies) {
                    String imdbRank = movie.getImdbRank()==251 ? "" : String.valueOf(movie.getImdbRank());
                    String doubanRank = movie.getDoubanRank()==251 ? "" : String.valueOf(movie.getDoubanRank());
                    String text = movie.getITitle() + "," + movie.getDTitle() + "," + movie.getYear()+ "," +
                            imdbRank + "," + movie.getImdbViewer() + "," + doubanRank + "," + movie.getDoubanViewer() +
                            "," + movie.getGenres() + "," + movie.getCountry() + "," + movie.getILink() + "," +
                            movie.getDLink()+"\n";
                    writer.write(text);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            finally {
                writer.flush();
                writer.close();
            }
        }
    }

}