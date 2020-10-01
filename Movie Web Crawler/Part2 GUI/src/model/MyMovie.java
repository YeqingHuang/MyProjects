package model;

import javafx.beans.property.*;

public class MyMovie {

    private final StringProperty ITitle = new SimpleStringProperty();
    private final StringProperty DTitle = new SimpleStringProperty();
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final IntegerProperty imdbRank = new SimpleIntegerProperty();
    private final IntegerProperty imdbViewer = new SimpleIntegerProperty();
    private final IntegerProperty doubanRank = new SimpleIntegerProperty();
    private final IntegerProperty doubanViewer = new SimpleIntegerProperty();
    private final StringProperty genres = new SimpleStringProperty();
    private final StringProperty country = new SimpleStringProperty();
    private final StringProperty ILink = new SimpleStringProperty();
    private final StringProperty DLink = new SimpleStringProperty();

    private final ObjectProperty<Type> type = new SimpleObjectProperty<>();

    public String getITitle() {
        return ITitle.get();
    }

    public StringProperty ITitleProperty() {
        return ITitle;
    }

    public void setITitle(String ITitle) {
        this.ITitle.set(ITitle);
    }

    public String getDTitle() {
        return DTitle.get();
    }

    public StringProperty DTitleProperty() {
        return DTitle;
    }

    public void setDTitle(String DTitle) {
        this.DTitle.set(DTitle);
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public int getImdbRank() {
        return imdbRank.get();
    }

    public IntegerProperty imdbRankProperty() {
        return imdbRank;
    }

    public void setImdbRank(int imdbRank) {
        this.imdbRank.set(imdbRank);
    }

    public int getImdbViewer() {
        return imdbViewer.get();
    }

    public IntegerProperty imdbViewerProperty() {
        return imdbViewer;
    }

    public void setImdbViewer(int imdbViewer) {
        this.imdbViewer.set(imdbViewer);
    }

    public int getDoubanViewer() {
        return doubanViewer.get();
    }

    public IntegerProperty doubanViewerProperty() {
        return doubanViewer;
    }

    public void setDoubanViewer(int doubanViewer) {
        this.doubanViewer.set(doubanViewer);
    }

    public int getDoubanRank() {
        return doubanRank.get();
    }

    public IntegerProperty doubanRankProperty() {
        return doubanRank;
    }

    public void setDoubanRank(int doubanRank) {
        this.doubanRank.set(doubanRank);
    }

    public String getGenres() {
        return genres.get();
    }

    public StringProperty genresProperty() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres.set(genres);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getILink() {
        return ILink.get();
    }

    public StringProperty ILinkProperty() {
        return ILink;
    }

    public void setILink(String ILink) {
        this.ILink.set(ILink);
    }

    public String getDLink() {
        return DLink.get();
    }

    public StringProperty DLinkProperty() {
        return DLink;
    }

    public void setDLink(String DLink) {
        this.DLink.set(DLink);
    }

    public Type getType() {
        return type.get();
    }

    public ObjectProperty<Type> typeProperty() {
        return type;
    }

    public void setType(Type type) {
        this.type.set(type);
    }

}