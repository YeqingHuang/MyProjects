package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class read from a txt file and create an ArrayList with all the movies
 */
public class Model {

    private static ArrayList<MyMovie> fileReader(String filepath) throws FileNotFoundException {
        File file = new File(filepath);
        Scanner sc = new Scanner(file);
        ArrayList<MyMovie> movies = new ArrayList<>();

        while (sc.hasNextLine()){
            String movieEntry = sc.nextLine();

            // it will be in a sequence as follows:
            // Good Will Hunting,心灵捕手,1997,88,8.3,832814,84,8.9,511917,126 min,
            // Drama|Romance,USA,English,https://moviedata.douban.com/subject/1292656/,
            // https://www.imdb.com/title/tt0119217,B
            String[] fields = movieEntry.split(",");
            MyMovie movie = new MyMovie();
            movie.setITitle(fields[0]);
            movie.setDTitle(fields[1]);
            movie.setYear(Integer.parseInt(fields[2]));

            String imdbRank = fields[3];
            int rank1 = imdbRank.isEmpty()? 251:Integer.parseInt(imdbRank);
            movie.setImdbRank(rank1);
            movie.setImdbViewer(Integer.parseInt(fields[5]));

            String doubanRank = fields[6];
            int rank2 = doubanRank.isEmpty()? 251:Integer.parseInt(doubanRank);
            movie.setDoubanRank(rank2);
            movie.setDoubanViewer(Integer.parseInt(fields[8]));

            movie.setGenres(fields[10]);
            movie.setCountry(fields[11]);
            movie.setDLink(fields[13]);
            movie.setILink(fields[14]);

            String abbType = fields[15];
            Type type;
            if (abbType.equals("I")){
                type = Type.IMDB;
            }
            else if (abbType.equals("D")){
                type = Type.DOUBAN;
            }
            else {
                type = Type.BOTH;
            }
            movie.setType(type);

            // add this movie to the ArrayList
            movies.add(movie);
        }

        return movies;
    }

    public static ArrayList<MyMovie> getData(String filepath) throws FileNotFoundException {
        return fileReader(filepath);
    }
}