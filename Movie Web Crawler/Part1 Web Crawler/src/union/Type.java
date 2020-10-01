package union;

/**
 * This enum indicates if a movie is in both top250 lists
 * or it is just in one list
 */
public enum Type {

    BOTH,DOUBAN,IMDB;

    public String toString(){
        String type = "";
        switch (this) {
            case BOTH:
                type = "B";
                break;
            case DOUBAN:
                type = "D";
                break;
            case IMDB:
                type = "I";
        }
        return type;
    }
}
