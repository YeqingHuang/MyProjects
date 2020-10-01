package imdb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class IMDBListing {

    public static ArrayList<IMDBMovie> getAllMovies() throws IOException {

        ArrayList<IMDBMovie> movies = new ArrayList<>(250);

        final String sourceURL = "https://www.imdb.com/chart/top/?ref_=nv_mv_250";
        Document document = Jsoup.connect(sourceURL).get();
        Element table = document.selectFirst("tbody.lister-list");
        Elements rows = table.getElementsByTag("tr");

        for (Element row : rows) {
            // create a new movie
            IMDBMovie movie = new IMDBMovie();

            // part1: get rating, viewerCount and movieID from posterColumn
            Elements posterElements = row.select("td.posterColumn");

            Elements ir = posterElements.first().select("span[name=ir]");
            Double originalRating = Double.parseDouble(ir.first().attr("data-value"));
            Double rating = Math. round(originalRating * 10.0) / 10.0;

            Elements nv = posterElements.first().select("span[name=nv]");
            Integer numberOfViewers = Integer.parseInt(nv.first().attr("data-value"));

            // the originalURL is long, what we need is a substring between the 2nd and 3rd '/'
            // "/title/tt0111161/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=e31d89dd-322d-4646-8962-327b..."
            Element urlElement = posterElements.select("a").first();
            String originalURL = urlElement.attr("href");
            String movieID = originalURL.substring(7,16);

            movie.setRating(rating);
            movie.setViewerCount(numberOfViewers);
            movie.setMovieID(movieID);
            movie.setMovieURL("https://www.imdb.com/title/" + movieID);

            // part2: get ranking, movie title and year from titleColumn
            Elements titleElement = row.select("td.titleColumn");
            String info = titleElement.text();
            // the info is in a format of "151. Three Billboards Outside Ebbing, Missouri (2017)\n"
            // we don't want commas in the movie title, it will be confused with our delimiter
            Integer rank = Integer.parseInt(info.substring(0, info.indexOf(".")));
            String originalTitle = info.substring(info.indexOf(" ") + 1, info.lastIndexOf(" "));
            String title = originalTitle.replaceAll(",","");
            String year = info.substring(info.indexOf("(") + 1, info.indexOf(")"));

            movie.setRank(rank);
            movie.setTitle(title);
            movie.setYear(year);

            System.out.println(movie.toString());
            movies.add(movie);
        }
        return movies;
    }

    // export it to txt, each movie object is one line separated by comma
    public static void exportMovies(ArrayList<IMDBMovie> movies) {
        ArrayList<String> movieStrings = new ArrayList<>(250);

        // convert each movie to string
        for (IMDBMovie movie : movies) {
            movieStrings.add(movie.toString());
        }
        // store them in a txt file
        try (FileOutputStream out = new FileOutputStream("imdb250.txt");) {
            for (String string : movieStrings) {
                out.write(string.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<IMDBMovie> movies250 = getAllMovies();
        exportMovies(movies250);
    }
}