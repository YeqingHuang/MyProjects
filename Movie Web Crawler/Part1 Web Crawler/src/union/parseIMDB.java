package union;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * given an IMDB movie details page URL, get below movie information:
 * 1) if movie type is BOTH or IMDB, we will get runtime, genres, language, country
 * 2) if movie type is DOUBAN, apart from the four fields mentioned in 1)
 * we also need to get ITitle, year, IRating, IViewerCount
 */
public class parseIMDB {

    public static ArrayList<String> getDetails(String URL) throws IOException {

        final Document document = Jsoup.connect(URL).get();
        Elements pageContents = document.select("div.flatland");
        ArrayList<String> result = new ArrayList<>();

        // TODO: from the topSection, we will get movieTitle, year, rating and viewerCount
        Element topSection = pageContents.select("div.title_bar_wrapper").first();

        Element ratingElement = topSection.getElementsByTag("strong").first();
        String ratingSentence = ratingElement.attr("title");
        // the sentence is like "8.3 based on 688,428 user ratings"
        // we need to parse it to get rating = 8.3, viewerCount = 688428
        String[] words = ratingSentence.split(" ");
        String rating = words[0];
        String viewerCount = words[3].replaceAll(",", "");
        result.add(rating);
        result.add(viewerCount);

        Element titleElement = topSection.selectFirst("div.title_wrapper");
        String titleYear = titleElement.select("h1").first().text();
        // titleYear is in a format like "Amélie (2001)"
        // we need to split it using last space of the string
        int index = titleYear.lastIndexOf(" ");
        String movieTitle = titleYear.substring(0, index).replaceAll(",","");
        String year = titleYear.substring(index + 2, titleYear.length() - 1);
        result.add(movieTitle);
        result.add(year);


        // TODO: from the middleSection, we will get genres of the movie
        Element middleSection = pageContents.select("div#titleStoryLine").first();
        List<String> genresList = new ArrayList<>();
        for (Element element : middleSection.children()) {
            Element h4Element = element.getElementsByTag("h4").first();
            if (h4Element == null) {
                continue;
            }
            if (h4Element.text().startsWith("Genres")) {
                Elements genresElements = element.select("a");
                for (Element genres : genresElements) {
                    genresList.add(genres.text());
                }
            }
        }
        result.add(genresList.toString());


        // TODO: from the bottomSection, we will get country, language, runtime
        Element bottomSection = pageContents.select("div#titleDetails").first();
        List<String> countryList = new ArrayList<>();
        List<String> languageList = new ArrayList<>();
        String runtimeStr = "";
        for (Element element : bottomSection.children()) {
            Element h4Element = element.getElementsByTag("h4").first();
            if (h4Element == null) {
                continue;
            }
            if (h4Element.text().startsWith("Country")) {
                Elements countryElements = element.select("a");
                for (Element country : countryElements) {
                    countryList.add(country.text());
                }
            }
            else if (h4Element.text().startsWith("Language")) {
                Elements languageElements = element.select("a");
                for (Element language : languageElements) {
                    languageList.add(language.text());
                }
            }
            else if (h4Element.text().startsWith("Runtime")) {
                Element runtimeElement = element.getElementsByTag("time").first();
                // if there are multiple runtimes with different movie versions
                // we only use the first runtime
                runtimeStr = runtimeElement.text();
            }
        }
        result.add(countryList.toString());
        result.add(languageList.toString());
        result.add(runtimeStr);

        return result;
    }

    public static void setDetails(Movie movie) throws IOException {
        String sourceURL = movie.getILink();
        final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                " AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/73.0.3683.86 Safari/537.36";
        final Document document = Jsoup.connect(sourceURL).userAgent(userAgent)
                .header("Accept-Language", "en")
                .header("Accept-Encoding", "gzip,deflate,sdch")
                .referrer("http://www.google.com")
                .get();

        Elements pageContents = document.select("div.flatland");
        if (movie.getType().equals(Type.DOUBAN)) {
            // TODO: from the topSection, we will get movieTitle, year, rating and viewerCount
            Element topSection = pageContents.select("div.title_bar_wrapper").first();

            Element ratingElement = topSection.getElementsByTag("strong").first();
            String ratingSentence = ratingElement.attr("title");
            // the sentence is like "8.3 based on 688,428 user ratings"
            // we need to parse it to get rating = 8.3, viewerCount = 688428
            String[] words = ratingSentence.split(" ");
            String rating = words[0];
            String viewerCount = words[3].replaceAll(",", "");

            Element titleElement = topSection.selectFirst("div.title_wrapper");
            String titleYear = titleElement.select("h1").first().text();
            // titleYear is in a format like "Amélie (2001)"
            // we need to split it using last space of the string
            int index = titleYear.lastIndexOf(" ");
            String movieTitle = titleYear.substring(0, index).replaceAll(",","");
            String year = titleYear.substring(index + 2, titleYear.length() - 1);

            movie.setIRating(rating);
            movie.setIViewerCount(viewerCount);
            movie.setITitle(movieTitle);
            movie.setYear(year);
        }

        // TODO: from the middleSection, we will get genres of the movie
        Element middleSection = pageContents.select("div#titleStoryLine").first();
        List<String> genresList = new ArrayList<>();
        for (Element element : middleSection.children()) {
            Element h4Element = element.getElementsByTag("h4").first();
            if (h4Element == null) {
                continue;
            }
            if (h4Element.text().startsWith("Genres")) {
                Elements genresElements = element.select("a");
                for (Element genres : genresElements) {
                    genresList.add(genres.text());
                }
            }
        }
        movie.setGenres(genresList);


        // TODO: from the bottomSection, we will get country, language, runtime
        Element bottomSection = pageContents.select("div#titleDetails").first();
        List<String> countryList = new ArrayList<>();
        List<String> languageList = new ArrayList<>();
        String runtime = "";
        for (Element element : bottomSection.children()) {
            Element h4Element = element.getElementsByTag("h4").first();
            if (h4Element == null) {
                continue;
            }
            if (h4Element.text().startsWith("Country")) {
                Elements countryElements = element.select("a");
                for (Element country : countryElements) {
                    countryList.add(country.text());
                }
            }
            else if (h4Element.text().startsWith("Language")) {
                Elements languageElements = element.select("a");
                for (Element language : languageElements) {
                    languageList.add(language.text());
                }
            }
            else if (h4Element.text().startsWith("Runtime")) {
                Element runtimeElement = element.getElementsByTag("time").first();
                // if there are multiple runtimes, we only use the first one
                runtime = runtimeElement.text();
            }
        }
        movie.setCountry(countryList);
        movie.setLanguage(languageList);
        movie.setRuntime(runtime);

        System.out.println("Done for imdb movie " + movie.getILink());
    }

    public static void main(String[] args) throws IOException {
        // the fields would be in a sequence as follows:
        // [7.7, 357147, Black Hawk Down, 2001, [Drama, History, War], [USA, UK], [English, Somali, Arabic], 144 min]

        String source2 = "https://www.imdb.com/title/tt0265086/";
        String source3 = "https://www.imdb.com/title/tt0110413/";

        ArrayList<String> info2 = getDetails(source2);
        System.out.println(info2.toString());

        ArrayList<String> info3 = getDetails(source3);
        System.out.println(info3.toString());

    }
}