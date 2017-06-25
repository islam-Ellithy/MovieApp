package com.example.islam.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by islam on 12/4/2016.
 */

class MovieAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Movie> movies;
    private ImageView ivPoster;


    // references to our images

    public MovieAdapter(Context c,ArrayList<Movie> mMovies)
    {
        context = c ;
        movies = mMovies;
    }

    public MovieAdapter() {
        movies = new ArrayList<Movie>();
    }

    @Override
    public int getCount() {

        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.grid_list_item,null);

            ivPoster = (ImageView)view.findViewById(R.id.movie_poster);

            int h = context.getResources().getDisplayMetrics().densityDpi;
//
            ivPoster.setLayoutParams(new GridView.LayoutParams(h - 45, h - 39));

            ivPoster.setScaleType(ImageView.ScaleType.CENTER_CROP);

            view.setTag(ivPoster);
        } else {
            ivPoster = (ImageView) view.getTag();
        }

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + movies.get(position).getPoster()).into(ivPoster);

        return view;
    }

}
