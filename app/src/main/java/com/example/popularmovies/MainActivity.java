package com.example.popularmovies;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback{

    private boolean mTwoPane ;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                    .commit();
        }

    }
    @Override
    public void onItemSelected(MovieInfo movie) {
        Bundle args = new Bundle();
        args.putParcelable("MOVIE", movie);
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.


            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtras(args);
            startActivity(intent);
        }
    }
    public void onFavoriteClicked (View v){
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        fragment.onFavoriteClicked(v);
    }
    public void onTrailerClicked(View v) {

        DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        fragment.onTrailerClicked(v);
    }

//52255459a70d4a3982e8fac6baf6fa15
    ///discover/movie/?certification_country=US&certification=R&sort_by=vote_average.desc
    //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=52255459a70d4a3982e8fac6baf6fa15



}
