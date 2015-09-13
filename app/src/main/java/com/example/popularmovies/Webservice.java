package com.example.popularmovies;

import java.util.List;

import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Артем on 12.09.2015.
 */

public class Webservice {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org")
            .build();
    MoviesService service = retrofit.create(MoviesService.class);
}
