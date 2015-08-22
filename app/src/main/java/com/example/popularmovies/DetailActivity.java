package com.example.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    public static class DetailFragment extends Fragment {
        public DetailFragment() {
            // Required empty public constructor
        }

        private final String LOG_TAG = "PopularMovies";
        private String MovieId = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            Intent intent = getActivity().getIntent();

            View rootView =  inflater.inflate(R.layout.detail_fragment, container, false);

            if (intent != null){
                ((TextView) rootView.findViewById(R.id.textView_overview)).setText(intent.getStringExtra("overview"));
                ((TextView) rootView.findViewById(R.id.textView_rating)).setText(intent.getStringExtra("rating"));
                ((TextView) rootView.findViewById(R.id.textView_title)).setText(intent.getStringExtra("title"));
                ((TextView) rootView.findViewById(R.id.textView_releaseDate)).setText(intent.getStringExtra("releaseDate"));
                Picasso.with(getActivity())
                        .load(intent.getStringExtra("bigPoster"))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into((ImageView) rootView.findViewById(R.id.imageView_big_poster));


                //(TextView) rootView.findViewById(R.id.textView_overview).setText();
                //(TextView) rootView.findViewById(R.id.textView_rating).s
            }

            return rootView;
        }
    }

}
