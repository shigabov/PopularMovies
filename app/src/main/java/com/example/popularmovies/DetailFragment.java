package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import android.view.View.OnClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Артем on 23.08.2015.
 */
public class DetailFragment extends Fragment  {

    private final String LOG_TAG = "PopularMovies";
    MovieInfo  movie = new MovieInfo();

    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static class ViewHolder {
        public final TextView titleView;
        public final TextView overviewView;
        public final ImageButton favoriteBtn;
        public final TextView releaseView;
        public final ListView trailersLV;
        public final ListView reviewsLV;
        public final RatingBar ratingView;

        public ViewHolder(View v) {
            overviewView = (TextView) v.findViewById(R.id.textView_overview);
            titleView = (TextView) v.findViewById(R.id.textView_title);
            favoriteBtn  = (ImageButton) v.findViewById(R.id.imageBtn_favorite);
            releaseView  = (TextView) v.findViewById(R.id.textView_releaseDate);
            trailersLV = (ListView) v.findViewById(R.id.listView_trailers);
            reviewsLV = (ListView) v.findViewById(R.id.listView_reviews);
            ratingView = (RatingBar) v.findViewById(R.id.ratingBar);
        }
    }

    private void updateMovie(){
        FetchMovieTrailersReview fetchMovieTrailers = new FetchMovieTrailersReview();
        fetchMovieTrailers.execute("trailers");
        FetchMovieTrailersReview fetchMovieReviews = new FetchMovieTrailersReview();
        fetchMovieReviews.execute("reviews");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle args = getArguments();

        if (args != null){

            View rootView =  inflater.inflate(R.layout.detail_fragment, container, false);

            ViewHolder viewHolder = new ViewHolder(rootView);

            movie = args.getParcelable("MOVIE");

            viewHolder.overviewView.setText(movie.overview);
            viewHolder.ratingView.setRating((float) movie.voteAverage);
            viewHolder.titleView.setText(movie.originalTitle);
            viewHolder.releaseView.setText(movie.releaseDate);

            if (movie.isFavorite(getActivity())) viewHolder.favoriteBtn.setImageResource(R.drawable.star_2);
            else viewHolder.favoriteBtn.setImageResource(R.drawable.star);

            //adapterTrailer = new ArrayAdapter<>(getActivity(),R.layout.list_view_trailer,R.id.textView_trailer);
            trailerAdapter = new TrailerAdapter(getActivity());
            reviewAdapter = new ReviewAdapter(getActivity());

            viewHolder.trailersLV.setAdapter(trailerAdapter);
            viewHolder.reviewsLV.setAdapter(reviewAdapter);

            Picasso.with(getActivity())
                    .load(movie.bigPosterPath)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into((ImageView) rootView.findViewById(R.id.imageView_big_poster));


            //(TextView) rootView.findViewById(R.id.textView_overview).setText();
            //(TextView) rootView.findViewById(R.id.textView_rating).s
            updateMovie();
            return rootView;
        }
        else return null;
    }

    public void onTrailerClicked(View v) {
        final String YOUTUBE_URL = "http://www.youtube.com/watch";
        String source = (String) v.getTag();
        Uri uri = Uri.parse(YOUTUBE_URL).buildUpon().appendQueryParameter("v",source).build();
        Log.v(LOG_TAG, uri.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //intent.addCategory(Intent.CATEGORY_APP_BROWSER);
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
        else Log.v(LOG_TAG,"Activity not found!");
    }

    public void onFavoriteClicked(View v) {

        ViewHolder viewHolder = new ViewHolder(v);

        MovieDBOpenHelper movieDBOpenHelper = new MovieDBOpenHelper(getActivity());
        SQLiteDatabase movieDB = movieDBOpenHelper.getWritableDatabase();

        movie.changeFavorite(movieDB);

        movieDB.close();
        movieDBOpenHelper.close();

        if (movie.isFavorite) {
            viewHolder.favoriteBtn.setImageResource(R.drawable.star_2);
        } else viewHolder.favoriteBtn.setImageResource(R.drawable.star);
        v.refreshDrawableState();

    }

    public class FetchMovieTrailersReview extends AsyncTask<String, Void, String> {
        private final String LOG_TAG = "PopularMovies";
        private final int MAX_COUNT = 20;
        private String fetchType;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String api_key = "52255459a70d4a3982e8fac6baf6fa15";
            String jsonString = null;
            fetchType = params[0];
            try {

                String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/[id]/[type]?".replace("[id]", movie.id).replace("[type]",params[0]);
                Log.v(LOG_TAG,MOVIEDB_BASE_URL);

                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, api_key)
                        .build();
                //Log.v(LOG_TAG,builtUri.toString());
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                    //Log.v(LOG_TAG,"inputStream is null");
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                    //Log.v(LOG_TAG,"Nothing");
                }

                jsonString = buffer.toString();

            } catch (Exception e) {

                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                //return null;
                Log.v(LOG_TAG, e.getMessage());
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.v(LOG_TAG, e.getMessage());
                    }
                }
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    if (fetchType.equals("trailers")) {
                        //adapterTrailer.addAll(parseTrailers(result));;
                        trailerAdapter.addAll(parseTrailers(result));
                        trailerAdapter.notifyDataSetChanged();
                        Log.v(LOG_TAG,"parse trailer");
                    }
                    else {
                        reviewAdapter.addAll(parseReviews(result));
                        reviewAdapter.notifyDataSetChanged();
                        Log.v(LOG_TAG, "parse review");
                    }
                }
                catch (JSONException e){
                    Log.v(LOG_TAG,e.getMessage());
                }

            }
        }
    }
    public ArrayList<Trailer> parseTrailers (String jsonString) throws  JSONException {
        final String YOUTUBE = "youtube";
        final String NAME = "name";
        final String SOURCE = "source";
        ArrayList<Trailer> ret = new ArrayList<Trailer> ();

        JSONObject json = new JSONObject(jsonString);
        JSONArray jsonarray = json.getJSONArray(YOUTUBE);
        for(int i = 0; i < jsonarray.length(); i++) {
            JSONObject tr = jsonarray.getJSONObject(i);
            ret.add(new Trailer(tr.getString(NAME),tr.getString(SOURCE)));
        }
        return ret;
    }
    public ArrayList<Review> parseReviews (String jsonString) throws  JSONException {
        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";
        ArrayList<Review> ret = new ArrayList<Review> ();

        JSONObject json = new JSONObject(jsonString);
        JSONArray jsonarray = json.getJSONArray(RESULTS);

        for(int i = 0; i < jsonarray.length(); i++) {
            JSONObject tr = jsonarray.getJSONObject(i);
            ret.add(new Review(tr.getString(AUTHOR),tr.getString(CONTENT),tr.getString(URL)));
        }
        return ret;
    }
}
