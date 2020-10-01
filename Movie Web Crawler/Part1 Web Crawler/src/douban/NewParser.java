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
 * Use this class to parse a playlist manually created by a user
 * "https://www.douban.com/doulist/1518184/"
 */

public class NewParser {

    public static ArrayList<String> getOnePageMovies(String URL) throws IOException {
        System.out.println("We are parsing link: "+ URL);

        final Document document = Jsoup.connect(URL).get();

        Elements elements = document.select("div.doulist-item");
        ArrayList<String> movies = new ArrayList<>(25);

        for (Element element: elements) {
            StringBuilder sb = new StringBuilder();

            Element posElement = element.selectFirst("div.hd");
            sb.append(posElement.text());
            if (posElement.text().equals("156")){
                continue;
            }

            // TODO get the url of the movie
            Element urlElement = element.select("div.title a").first();
            String doubanURL = urlElement.attr("href");

            // TODO get the title of the movie
            Element titleElement = element.selectFirst("div.title");
            String title = titleElement.text();
            // most titles contain two parts, chinese title + " " + english/japanese/korean title
            // but there are titles only contain the first part part, i.e. chinese title
            // we need to check first
            String chnTitle, engTitle;
            if (title.contains(" ")){
                chnTitle = title.substring(0,title.indexOf(' ')).replaceAll(",","");
                engTitle = title.substring(title.indexOf(' ')+1,title.length()).replaceAll(",","");
            }
            else{
                chnTitle = title;
                engTitle = "";
            }
            sb.append("," + chnTitle + "," + engTitle);

            // TODO get the rating and viewers count of the movie
            Element ratingElement = element.selectFirst("div.rating");
            String rating = ratingElement.text();
            // rating is in a format of "9.2 (380480人评价)", we need to get 9.2 and 380480
            String ratingScore = rating.substring(0,rating.indexOf('(')-1);
            String viewerCount = rating.substring(rating.indexOf('(')+1, rating.length()-4);
            sb.append("," + ratingScore + "," + viewerCount);
            sb.append("," + doubanURL + "\n");

            movies.add(sb.toString());
        }
        return movies;
    }

    public static ArrayList<String> getAllMovies(String URL) throws IOException {
        ArrayList<String> movies = new ArrayList<>(250);
        ArrayList<String> urlList = new ArrayList<>(10);

        // generate the URL list first ( there are 10 pages, i.e. 10 URLs)
        String prefixURL = URL;
        urlList.add(prefixURL);
        // https://www.douban.com/doulist/1518184/
        // https://www.douban.com/doulist/1518184/?start=25&sort=seq&playable=0&sub_type=
        // https://www.douban.com/doulist/1518184/?start=50&sort=seq&playable=0&sub_type=
        for (int i=1; i<10; i++) {
            String resultURL = prefixURL + "?start=" + 25*i + "&sort=seq&playable=0&sub_type=";
            urlList.add(resultURL);
        }
        // call the getOnePageMovies method
        for (String url: urlList) {
            movies.addAll(getOnePageMovies(url));
        }
        return movies;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String sourceURL = "https://www.douban.com/doulist/1518184/";

        // STEP1: get 250 movie strings
        ArrayList<String> movies = getAllMovies(sourceURL);

        // STEP2: call DoubanDetails.getTargetURL() to go into each of the pages
        // and append targetURL to the original movieString
        ArrayList<String> modifiedMovies = new ArrayList<>(250);
        for (String movie:movies) {
            String doubanURL = movie.split(",")[5];
            String imdbURL = DoubanDetails.getTargetURL(doubanURL);
            String singleMovie = movie.substring(0,movie.length()-1) + "," + imdbURL + "\n";
            System.out.println(singleMovie);
            modifiedMovies.add(singleMovie);
            Thread.sleep(5000); // sleep for 5 seconds
        }

        // STEP3: export the edited strings to a txt file
        try (FileOutputStream out = new FileOutputStream("userlist.txt");) {
            for (String string : modifiedMovies) {
                out.write(string.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}