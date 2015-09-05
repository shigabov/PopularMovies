package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Артем on 30.08.2015.
 */
class ReviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Review> reviews = new ArrayList<>();

    public ReviewAdapter (Context c){
        context= c;
    }

    public static class ViewHolder {
        public final TextView reviewView;
        public final TextView authorView;

        public ViewHolder(View view) {
            authorView = (TextView) view.findViewById(R.id.textView_author);
            reviewView = (TextView) view.findViewById(R.id.textView_review);
        }
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        //ViewGroup view = (ViewGroup) getActivity().findViewById(R.id.layout_trailer);
        if(convertView == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_view_review, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.authorView.setText(reviews.get(position).author);
            viewHolder.reviewView.setText(reviews.get(position).content);
            return view;
        }
        else return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public int getCount() {
        return reviews.size();
    }
    public void addAll(ArrayList<Review> rv){
        reviews = rv;
    }
}