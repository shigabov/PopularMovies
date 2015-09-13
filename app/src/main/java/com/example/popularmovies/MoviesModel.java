package com.example.popularmovies;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Артем on 12.09.2015.
 */
public class MoviesModel implements Serializable {

    int page;
    ArrayList<Movie> results;
    int total_pages;
    int total_results;

    public class Movie implements Serializable {
        boolean adult;
        String backdrop_path;
        int[] genre_ids;
        int id;
        String original_language;
        String original_title;
        String overview;
        String release_date;
        String poster_path;
        String popularity;
        String title;
        boolean video;
        Double vote_average;
        int vote_count;
    }

}

