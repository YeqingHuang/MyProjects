package union;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MovieLibrary {
    // create a HashMap, key is ILink, value is the MyMovie object
    HashMap<String, Movie> library = new HashMap<>();

    public void readDoubanList(String pathname) throws FileNotFoundException {
        File file = new File(pathname);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()){
            String movieEntry = sc.nextLine();
            // it will be in a sequence of "Rank, Title, Rating, ViewerCount, doubanURL, imdbURL"
            String[] fields = movieEntry.split(",");

            Movie movie = new Movie();
            movie.setDRank(fields[0]);
            movie.setDTitle(fields[1]);
            movie.setDRating(fields[2]);
            movie.setDViewerCount(fields[3]);
            movie.setDLink(fields[4]);
            movie.setILink(fields[5]);

            // create a key-value pair for this new movie
            library.put(movie.getILink(),movie);
        }

    }

    public void readIMDBList(String pathname) throws FileNotFoundException {
        File file = new File(pathname);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String movieEntry = sc.nextLine();
            // it will be in a sequence of "Rank, Title, Year, Rating, ViewerCount, imdbURL"
            String[] fields = movieEntry.split(",");

            Movie movie;
            String imdbLink = fields[5];
            // check if our library already has this movie before creating a new one
            if (library.containsKey(imdbLink)) {
                movie = library.get(imdbLink);
            }
            else {
                movie = new Movie();
                movie.setILink(imdbLink);
                library.put(imdbLink,movie);
            }
            movie.setIRank(fields[0]);
            movie.setITitle(fields[1]);
            movie.setYear(fields[2]);
            movie.setIRating(fields[3]);
            movie.setIViewerCount(fields[4]);
        }

    }

    public void setType() {
        for (Map.Entry<String, Movie> e : library.entrySet()){
            Movie movie = e.getValue();
            if (movie.getDTitle().isEmpty()) {
                movie.setType(Type.IMDB);
            }
            else if (movie.getITitle().isEmpty()) {
                movie.setType(Type.DOUBAN);
            }
            else {
                movie.setType(Type.BOTH);
            }
        }
    }

    public void readUserList (String pathname) throws FileNotFoundException{
        File file = new File(pathname);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String movieEntry = sc.nextLine();
            // it will be in a sequence of "8,低俗小说,Pulp Fiction,8.8,612305,
            // https://movie.douban.com/subject/1291832/,https://www.imdb.com/title/tt0110912"
            String[] fields = movieEntry.split(",");
            String imdbLink = fields[6];

            Movie movie;
            if (library.containsKey(imdbLink)){
                movie = library.get(imdbLink);
                if (movie.getType().equals(Type.IMDB)) {
                    movie.setDTitle(fields[1]);
                    movie.setDRating(fields[3]);
                    movie.setDViewerCount(fields[4]);
                    movie.setDLink(fields[5]);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MovieLibrary movies = new MovieLibrary();
        // step1: read from two txt files(each has 250 lines) and setType
        movies.readDoubanList("./douban250.txt");
        movies.readIMDBList("./imdb250.txt");
        movies.setType();

        // step2: call setDetails() to get IMDB detail page info
        for (Map.Entry<String, Movie> e : movies.library.entrySet()) {
            Movie movie = e.getValue();
            parseIMDB.setDetails(movie);
        }

        // step3: get additional information for those movies which type is IMDB
        movies.readUserList("./userlist.txt");

        // step4: convert each movie to a string and export the strings to a txt file
        ArrayList<String> movieStrings = new ArrayList<>();
        for (Map.Entry<String, Movie> e : movies.library.entrySet()) {
            Movie movie = e.getValue();
            movieStrings.add(movie.toString());
        }

        try (FileOutputStream out = new FileOutputStream("final.txt");) {
            for (String string : movieStrings) {
                out.write(string.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}