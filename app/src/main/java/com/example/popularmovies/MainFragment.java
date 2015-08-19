package com.example.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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

    private final String LOG_TAG = "PopularMovies";

    public String sort_by = "popularity.desc";

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<MovieInfo> movies;

        public ImageAdapter(Context c) {
            mContext = c;
            movies = new ArrayList<MovieInfo>();
        }

        public int getCount() {
            return movies.size();
        }
        public void setItems(ArrayList<MovieInfo> list){
            movies = list;
        }

        public Object getItem(int position) {
            return null;
        }

        public MovieInfo getMovie(int position) {
            return movies.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }

            //Log.v(LOG_TAG, movies.get(position).posterPath);
            Picasso.with(mContext).load(movies.get(position).posterPath).into(imageView);
            return imageView;
        }

        // references to our images
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    final int  resCode = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();

        if (id==R.id.action_sort){
            Intent intent = new Intent(getActivity(),SortByActivity.class);
            intent.putExtra("sortBy",sort_by);
            startActivityForResult(intent, resCode);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == resCode) {
            if (resultCode == Activity.RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                //Log.v("PopularMovies","onActivityResult="+data.getStringExtra("sortBy"));
                sort_by = data.getStringExtra("sortBy");

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
        updateMovies();
    }

    private void updateMovies(){
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute(sort_by);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.main_fragment, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);

        imageAdapter = new ImageAdapter((getActivity()));

        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                MovieInfo movie = imageAdapter.getMovie(position);
                Intent intent = new Intent(getActivity(),DetailActivity.class).putExtra("Id", movie.id).putExtra("title", movie.originalTitle).putExtra("releaseDate",movie.releaseDate).putExtra("rating",movie.voteAverage).putExtra("poster",movie.posterPath).putExtra("bigPoster",movie.bigPosterPath).putExtra("overview",movie.overview);
                startActivity(intent);
            }
        });

        return rootView;
    }

    class MovieInfo {

        public String id;
        public String posterPath;
        public String bigPosterPath;
        public String originalTitle;
        public String overview;
        public String releaseDate;
        public String voteAverage;

        MovieInfo(String id, String posterPath, String bigPosterPath, String originalTitle, String overview, String releaseDate, String voteAverage) {
            this.id = id;
            this.posterPath = posterPath;
            this.bigPosterPath = bigPosterPath;
            this.originalTitle = originalTitle;
            this.overview = overview;
            this.releaseDate = releaseDate;
            this.voteAverage = voteAverage;
        }
    }

    public class FetchMovies extends AsyncTask<String, Void, ArrayList<MovieInfo>> {
        private final String LOG_TAG = "PopularMovies";
        private final int MAX_COUNT = 20;

        @Override
        protected ArrayList<MovieInfo> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String api_key = "52255459a70d4a3982e8fac6baf6fa15";
            String jsonString = null;
            try {

                final String MOVIEDB_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                Log.v(LOG_TAG,"params="+params[0]);


                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, params[0])
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
                Log.v(LOG_TAG,e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.v(LOG_TAG,e.getMessage());
                    }
                }
            }
            try {
                return parse(jsonString);
            }
            catch (JSONException e){
                Log.v(LOG_TAG,e.getMessage());
            }
            return null;
        }

        public ArrayList<MovieInfo> parse(String jsonString) throws  JSONException
        {

            final String MV_LIST = "results";
            final String MV_ID = "id";
            final String MV_POSTER = "poster_path";
            final String MV_OVERVIEW = "overview";
            final String MV_RELEASE_DATE = "release_date";
            final String MV_VOTE_AVERAGE = "vote_average";
            final String MV_ORIGINAL_TITLE = "original_title";
            final String IMAGE_BASE_PATH =  "http://image.tmdb.org/t/p/";
            final String IMAGE_SIZE = "w185";
            final String BIG_IMAGE_SIZE = "w342";
            ArrayList<MovieInfo> res = new ArrayList<MovieInfo>();


            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonarray = json.getJSONArray(MV_LIST);

            for(int i = 0; i < jsonarray.length() && i < MAX_COUNT; i++) {
                JSONObject movie = jsonarray.getJSONObject(i);
                res.add(new MovieInfo(movie.getString(MV_ID),IMAGE_BASE_PATH + IMAGE_SIZE+movie.getString(MV_POSTER),IMAGE_BASE_PATH + BIG_IMAGE_SIZE+movie.getString(MV_POSTER),movie.getString(MV_ORIGINAL_TITLE),movie.getString(MV_OVERVIEW),movie.getString(MV_RELEASE_DATE),movie.getString(MV_VOTE_AVERAGE)));
            }
           // Log.v(LOG_TAG,"added items = " + res.size());
            return res;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> result){
            if (result!=null){
                //movies = result;
                imageAdapter.setItems(result);
                imageAdapter.notifyDataSetChanged();
            }
        }
    }


}
