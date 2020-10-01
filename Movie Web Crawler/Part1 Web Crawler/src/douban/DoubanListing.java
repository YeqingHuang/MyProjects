package douban;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Use this class to get the Douban Top250 movies listing information
 * Most fields of DoubanMovie can be exacted here except for its outBoundURL
 * We will use another class called DoubanGetDetails to get outBoundURL
 */
public class DoubanListing {

    /**
     * Use this method to get the 25 movies on a single page
     * @param URL url of this page
     * @return an ArrayList of movies
     * @throws IOException
     */
    public static ArrayList<DoubanMovie> getOnePageMovies(String URL) throws IOException {
        System.out.println("We are parsing link: "+ URL);

        final Document document = Jsoup.connect(URL).get();
        // we will have 25 elements
        Elements elements = document.select("ol li");
        ArrayList<DoubanMovie> movies = new ArrayList<>(25);

        for (Element element : elements) {
            DoubanMovie movie = new DoubanMovie();
            // 1.get its ranking
            Element rankElement = element.selectFirst("em");
            String rank = rankElement.text();
            movie.setRank(Integer.parseInt(rank));

            // 2.get its URL
            Element urlElement = element.select("div.hd a").first();
            String url = urlElement.attr("href");
            movie.setDoubanURL(url);

            // 3.get its Chinese title
            Element titleElement = urlElement.select("span.title").first();
            String chineseTitle = titleElement.text();
            movie.setTitle(chineseTitle);

            // 4.get its rating
            Element ratingElement = element.select("div.star span.rating_num").first();
            String rating = ratingElement.text();
            movie.setRating(Double.parseDouble(rating));

            // 5.check how many people have rated the movie
            Element viewerElement = element.select("div.star span").last();
            String viewerCount = viewerElement.text();
            // we cannot use this string directly because it's in a format of "1539997人评价"
            // we need to get rid of the last three Chinese characters
            int count = Integer.parseInt(viewerCount.substring(0,viewerCount.length()-3));
            movie.setViewerCount(count);

            // append this new movie to the ArrayList
            movies.add(movie);
        }

        return movies;
    }

    /**
     * Use this method to get top 250 movies, i.e. call getOnePageMovies ten times
     * @return an ArrayList of movies
     * @throws IOException
     */
    public static ArrayList<DoubanMovie> getAllMovies() throws IOException {
        // we need to get 10 pages of movies, find the pattern of the URLs:
        // Page1: https://movie.douban.com/top250
        // Page2: https://movie.douban.com/top250?start=25&filter=
        // Page3: https://movie.douban.com/top250?start=50&filter=
        // Page25: https://movie.douban.com/top250?start=225&filter=

        ArrayList<DoubanMovie> movies = new ArrayList<>(250);
        // generate the 10 URLs so we can use them later
        String prefixURL = "https://movie.douban.com/top250";
        ArrayList<String> urlList = new ArrayList<>(10);
        urlList.add(prefixURL);  // add the first URL, generate the rest 9 URLs
        for (int i = 1; i < 10; i++) {
            String fullURL = prefixURL + "?start=" + Integer.toString(i*25) + "&filter=";
            urlList.add(fullURL);
        }
        // call the getOnePageMovies method in a for loop
        for (String url : urlList) {
            movies.addAll(getOnePageMovies(url));
        }
        return movies;
    }

    // convert it to txt, we will try to export it to csv file later
    public static void exportMovies(ArrayList<DoubanMovie> movies) {
        // convert each movie to string
        ArrayList<String> movieStrings = new ArrayList<>(250);
        for (DoubanMovie movie : movies) {
            movieStrings.add(movie.toString());
        }
        // store them in a txt file
        try (FileOutputStream out = new FileOutputStream("try.txt");) {
            for (String string : movieStrings) {
                out.write(string.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setURL(DoubanMovie movie) throws IOException {
        String sourceURL = movie.getDoubanURL();
        String imdbURL = DoubanDetails.getTargetURL(sourceURL);
        movie.setImdbURL(imdbURL);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<DoubanMovie> movies250 = getAllMovies();

        for (DoubanMovie movie : movies250) {
            setURL(movie);
            System.out.println(movie.toString());
            sleep(5000); // sleep for 5 seconds
        }
        exportMovies(movies250);
    }
}