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
class TrailerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Trailer> trailers = new ArrayList<>();

    public TrailerAdapter (Context c){
        context= c;
    }

    public static class ViewHolder {
        public final TextView descriptionView;

        public ViewHolder(View view) {
            descriptionView = (TextView) view.findViewById(R.id.textView_trailer);
        }
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        //ViewGroup view = (ViewGroup) getActivity().findViewById(R.id.layout_trailer);
        if(convertView == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_view_trailer, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.descriptionView.setText(trailers.get(position).name);
            viewHolder.descriptionView.setTag(trailers.get(position).source);
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
        return trailers.size();
    }
    public void addAll(ArrayList<Trailer> tr){
        trailers = tr;
    }
}