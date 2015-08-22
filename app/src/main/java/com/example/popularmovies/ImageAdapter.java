package com.example.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Артем on 21.08.2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MovieInfo> movies;

    public ImageAdapter(Context c) {
        mContext = c;
        movies = new ArrayList<>();
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
        Picasso.with(mContext).load(movies.get(position).posterPath).placeholder(R.drawable.placeholder)
                .error(R.drawable.error).into(imageView);
        return imageView;
    }

    // references to our images
}
