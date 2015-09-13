package com.example.popularmovies;

import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Артем on 12.09.2015.
 */
public interface MoviesService {

        @GET("/3/discover/movie")
        Call<ResponseBody> listMovies(@Query("sort_by") String sort,@Query("api_key") String api_key);

        @GET("/3/movie/{id}/{type}?")
        List<String> listReviews (@Path("id") String id, @Path("type") String type);

        @GET("/3/movie/{id}/{type}?")
        List<String> listTrailers(@Path("id") String id, @Path("type") String type);

}
