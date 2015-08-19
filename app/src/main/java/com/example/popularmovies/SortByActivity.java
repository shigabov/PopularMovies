package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Артем on 19.08.2015.
 */
public class SortByActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_by_activity);
        RadioButton id1 = (RadioButton) findViewById(R.id.radioButton_pop);
        RadioButton id2 = (RadioButton) findViewById(R.id.radioButton_rating);

        Intent intent = getIntent();
        if (intent != null){
            switch (intent.getStringExtra("sortBy")) {
                case "popularity.desc":
                    id1.toggle();
                    break;
                case "vote_average.desc":
                    id2.toggle();
                    break;
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        Intent intent = new Intent(this,MainActivity.class);
        switch(view.getId()) {
            case R.id.radioButton_pop:
                if (checked) intent.putExtra("sortBy","popularity.desc");
                break;
            case R.id.radioButton_rating:
                if (checked) intent.putExtra("sortBy","vote_average.desc");
                break;
        }
        setResult (RESULT_OK, intent);
        finish();
    }
}
