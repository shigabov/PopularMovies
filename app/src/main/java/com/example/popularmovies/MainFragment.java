package com.example.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import org.json.JSONException;

import java.io.BufferedReader;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    public ImageAdapter imageAdapter;
    public ArrayList<MovieInfo> movies = new ArrayList<>();
    public String MOVIE_KEY = "movies";
    public String MOVIE_SORT = "sort";

    private final String LOG_TAG = "PopularMovies";

    public String sort_by = "popularity.desc";
    public final int resCode = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    public interface Callback {
        public void onItemSelected(MovieInfo movie);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_KEY)) {
            ArrayList<MovieInfo> parcelMovie = (ArrayList<MovieInfo>) savedInstanceState.get(MOVIE_KEY);
            if (parcelMovie != null) {
                for (MovieInfo movie : parcelMovie) {
                    movies.add(movie.getMovie());
                }
            }
            sort_by = savedInstanceState.getString(MOVIE_SORT);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_sort) {
            Intent intent = new Intent(getActivity(), SortByActivity.class);
            intent.putExtra("sortBy", sort_by);
            startActivityForResult(intent, resCode);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == resCode) {
            if (resultCode == Activity.RESULT_OK) {
                sort_by = data.getStringExtra("sortBy");
                updateMovies();

            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main_fragment_menu, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (movies.size() == 0) updateMovies();
        else imageAdapter.setItems(movies);
    }

    private void updateMovies() {
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute(sort_by);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);

        imageAdapter = new ImageAdapter((getActivity()));

        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                MovieInfo movie = imageAdapter.getMovie(position);

                ((Callback) getActivity()).onItemSelected(movie);
            }
        });

        return rootView;
    }

    public class FetchMovies extends AsyncTask<String, Void, ArrayList<MovieInfo>> {
        final String LOG_TAG = "PopularMovies";
        final int MAX_COUNT = 20;
        final String IMAGE_SIZE = "w342";
        final String BIG_IMAGE_SIZE = "w342";
        final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/";
        final String MV_LIST = "results";
        final String MV_ID = "id";
        final String MV_POSTER = "poster_path";
        final String MV_OVERVIEW = "overview";
        final String MV_RELEASE_DATE = "release_date";
        final String MV_VOTE_AVERAGE = "vote_average";
        final String MV_ORIGINAL_TITLE = "original_title";


        @Override
        protected ArrayList<MovieInfo> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String api_key = "52255459a70d4a3982e8fac6baf6fa15";
            String jsonString = null;

            Log.v(LOG_TAG, "params[0]=" + params[0]);
            if (!params[0].equals("favorite")) {
                try {
                    final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                    final String SORT_BY = "sort_by";
                    final String API_KEY = "api_key";

                    Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                            .appendQueryParameter(SORT_BY, params[0])
                            .appendQueryParameter(API_KEY, api_key)
                            .build();

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
                try {
                    return parse(jsonString);
                } catch (JSONException e) {
                    Log.v(LOG_TAG, e.getMessage());
                }
            } else {
                //Log.v(LOG_TAG,"favorite");
                MovieDBOpenHelper movieDBOpenHelper = new MovieDBOpenHelper(getActivity());
                try {
                    SQLiteDatabase movieDB = movieDBOpenHelper.getReadableDatabase();
                    return parse(movieDB.rawQuery("select * from movies", null));
                } catch (SQLException e) {
                    Log.v(LOG_TAG, e.getMessage());
                } finally {
                    movieDBOpenHelper.close();
                }
            }
            return null;
        }

        public ArrayList<MovieInfo> parse(Cursor c) throws SQLException {

            ArrayList<MovieInfo> ret = new ArrayList<>();
            //Log.v(LOG_TAG, "cnt=" + c.getCount());

            c.moveToFirst();

            //Log.v(LOG_TAG, "release_date=" + c.getString(2));

            while (!c.isAfterLast()) {
                ret.add(new MovieInfo(c.getString(0),
                        IMAGE_BASE_PATH + IMAGE_SIZE + c.getString(5),
                        IMAGE_BASE_PATH + BIG_IMAGE_SIZE + c.getString(5),
                        c.getString(1),
                        c.getString(3),
                        c.getString(2),
                        c.getDouble(4)));
                c.moveToNext();
            }

            c.close();

            return ret;
        }

        public ArrayList<MovieInfo> parse(String jsonString) throws JSONException {


            ArrayList<MovieInfo> res = new ArrayList<>();


            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonarray = json.getJSONArray(MV_LIST);

            for (int i = 0; i < jsonarray.length() && i < MAX_COUNT; i++) {
                JSONObject movie = jsonarray.getJSONObject(i);
                res.add(new MovieInfo(movie.getString(MV_ID),
                        IMAGE_BASE_PATH + IMAGE_SIZE + movie.getString(MV_POSTER),
                        IMAGE_BASE_PATH + BIG_IMAGE_SIZE + movie.getString(MV_POSTER),
                        movie.getString(MV_ORIGINAL_TITLE),
                        movie.getString(MV_OVERVIEW),
                        movie.getString(MV_RELEASE_DATE),
                        movie.getDouble(MV_VOTE_AVERAGE)));
            }
            // Log.v(LOG_TAG,"added items = " + res.size());
            return res;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> result) {
            if (result != null) {
                movies = result;
                imageAdapter.setItems(movies);
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        // Save the user's current game state
        ArrayList<MovieInfo> parselMovie = new ArrayList<>();
        for (MovieInfo movie : movies) {
            parselMovie.add(movie);
        }
        savedInstanceState.putParcelableArrayList(MOVIE_KEY, parselMovie);
        savedInstanceState.putString(MOVIE_SORT, sort_by);
        //Log.v(LOG_TAG,"onSaveInstanceState");
    }
}
