package douban;

public class DoubanMovie {
    private String title;
    private String doubanURL;
    private String imdbURL;

    private Integer rank;
    private Double rating;
    private Integer viewerCount;

    public String getDoubanURL() {
        return doubanURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDoubanURL(String doubanURL) {
        this.doubanURL = doubanURL;
    }

    public void setImdbURL(String imdbURL) {
        this.imdbURL = imdbURL;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setViewerCount(Integer viewerCount) {
        this.viewerCount = viewerCount;
    }

    @Override
    public String toString() {
        return rank + "," + title +  "," + rating + "," + viewerCount + ","
                + doubanURL  + "," + imdbURL + "\n";
    }

}