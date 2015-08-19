package com.example.popularmovies;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }


//52255459a70d4a3982e8fac6baf6fa15
    ///discover/movie/?certification_country=US&certification=R&sort_by=vote_average.desc
    //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=52255459a70d4a3982e8fac6baf6fa15



}
