import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Double has disabled its official API for personal use
 * We will use another method by using a URL begins with:
 * "https://movie.douban.com/j/subject_suggest?q="
 * This idea comes from https://github.com/zce/douban-api-proxy/issues/15
 */
public class JsonSearch {

    private static String getJSON(String keyword) throws Exception {
        String URL = "https://movie.douban.com/j/subject_suggest?q=" + keyword;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + "80.0.3987.163 Safari/537.36")
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String result = response.body();
        return result.substring(1, result.length()-1);
    }

    public static void main(String[] args) throws Exception {
        String keyword = "tt0213847";
        String keyword1 = "tt2096673";
        // the result would be in a format as follows:
        // {"episode":"","img":"https://img9.doubanio.com\/view\/photo\/s_ratio_poster\/public\/p2448029416.jpg",
        // "title":"西西里的美丽传说","url":"https:\/\/movie.douban.com\/subject\/1292402\/?suggest=tt0213847",
        // "type":"movie","year":"2000","sub_title":"Malèna","id":"1292402"}
        try {
            JSONObject obj = new JSONObject(getJSON(keyword));
            JSONObject obj1 = new JSONObject(getJSON(keyword1));

            String title = obj.getString("title");
            String subtitle = obj.getString("sub_title");
            System.out.println(obj);
            System.out.println(title);
            System.out.println(subtitle);

            String title1 = obj1.getString("title");
            String subtitle1 = obj1.getString("sub_title");
            System.out.println(obj1);
            System.out.println(title1);
            System.out.println(subtitle1);
        }catch (JSONException err){
            System.out.println(err);
        }
    }
}
