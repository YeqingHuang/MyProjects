package imdb;

public class IMDBMovie {
    private String title;
    private String movieID;
    private String movieURL; // generated from movieID

    private Integer rank;
    private Double rating;
    private Integer viewerCount;
    private String year;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    // for example, for movie https://www.imdb.com/title/tt0111161
    // its unique movieID is tt0111161
    public void setMovieURL(String movieURL) {
        this.movieURL = movieURL;
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

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return rank + "," + title +  "," + year + "," + rating + "," + viewerCount + ","
                + movieURL + "\n";
    }

}
