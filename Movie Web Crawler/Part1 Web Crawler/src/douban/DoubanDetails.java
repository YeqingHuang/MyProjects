package douban;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Use this class to go to the movie details page and get iMDb url of the movie
 * We should not send requests too frequently, otherwise IP address will be blocked
 * Please refer to https://www.douban.com/robots.txt
 */
public class DoubanDetails {

    public static String getTargetURL(String sourceURL) throws IOException {
        String resultURL = "";
        try {
            Document document = Jsoup.connect(sourceURL).get();
            Element info = document.getElementById("info");
            for (Element element : info.getAllElements()) {
                // other element attributes also use href, such as director/actor page links
                // we only want the string which contains "imdb"
                if (element.attr("href").contains("imdb")) {
                    resultURL = element.attr("href");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return resultURL;
    }

    public static void main(String[] args) throws IOException {
        String testURL1 = "https://movie.douban.com/subject/1309046/"; // failure
        String testURL2 = "https://movie.douban.com/subject/1292720/"; // success
        System.out.println(getTargetURL(testURL1));
        System.out.println(getTargetURL(testURL2));
    }
}