package union;

import java.util.List;

public class Movie {
    // fields extracted from Douban website
    private String DTitle = "";
    private String DLink = "";
    private String DRank = "";
    private String DRating = "";
    private String DViewerCount = "";

    // fields extracted from IMDb website
    private String ITitle = "";
    private String ILink = "";
    private String IRank = "";
    private String IRating = "";
    private String IViewerCount = "";

    // general information of the movie
    // also extracted from IMDb website
    private String year;
    private String runtime;
    private List<String> genres;
    private List<String> language;
    private List<String> country;

    // after reading the two input files, we can decide
    // if a movie is in both lists or just in one list
    private Type type;

    public String getDTitle() {
        return DTitle;
    }

    public String getDLink() {
        return DLink;
    }

    public String getDRank() {
        return DRank;
    }

    public String getDRating() {
        return DRating;
    }

    public String getDViewerCount() {
        return DViewerCount;
    }

    public String getITitle() {
        return ITitle;
    }

    public String getILink() {
        return ILink;
    }

    public String getIRank() {
        return IRank;
    }

    public String getIRating() {
        return IRating;
    }

    public String getIViewerCount() {
        return IViewerCount;
    }

    public String getYear() {
        return year;
    }

    public String getRuntime() {
        return runtime;
    }

    public List<String> getGenres() {
        return genres;
    }

    private String showGenres() {
        if (genres == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(String str:genres){
            sb.append(str);
            sb.append("|");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private String showLanguage() {
        if (language == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(String str:language){
            sb.append(str);
            sb.append("|");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private String showCountry() {
        if (country == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(String str:country){
            sb.append(str);
            sb.append("|");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public List<String> getLanguage() {
        return language;
    }

    public List<String> getCountry() {
        return country;
    }

    public Type getType() {
        return type;
    }

    public void setDTitle(String DTitle) {
        this.DTitle = DTitle;
    }

    public void setDLink(String DLink) {
        this.DLink = DLink;
    }

    public void setDRank(String DRank) {
        this.DRank = DRank;
    }

    public void setDRating(String DRating) {
        this.DRating = DRating;
    }

    public void setDViewerCount(String DViewerCount) {
        this.DViewerCount = DViewerCount;
    }

    public void setITitle(String ITitle) {
        this.ITitle = ITitle;
    }

    public void setILink(String ILink) {
        this.ILink = ILink;
    }

    public void setIRank(String IRank) {
        this.IRank = IRank;
    }

    public void setIRating(String IRating) {
        this.IRating = IRating;
    }

    public void setIViewerCount(String IViewerCount) {
        this.IViewerCount = IViewerCount;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String toString() {
        String movieInfo = ITitle + "," + DTitle + "," + year + "," + IRank + "," + IRating + "," + IViewerCount
                + "," + DRank + "," + DRating + "," + DViewerCount + "," + runtime
                + "," + this.showGenres() + "," + this.showCountry() + "," + this.showLanguage()
                + "," + DLink + "," + ILink + "," + type + "\n";
        return movieInfo;
    }

}