package com.example.islam.movieapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * islam emam
 */


public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    GridView gridPosters;
    ProgressBar bar;
    Movie movie = new Movie();
    ArrayList<Movie> movieList;
    static String API_KEY = "f938081fe3aeb032354e55c5d152d05f";

    private OnTaskCompleted listener = new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(Movie mMovie) {
            movie.clone(mMovie);
        }

        @Override
        public Movie getMovie()
        {
            return movie ;
        }
    };

    public MainFragment() {
        movieList = new ArrayList<Movie>();

    }


    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);

        try {

            gridPosters = (GridView) fragment.findViewById(R.id.gridView_fragment);

            bar = (ProgressBar) fragment.findViewById(R.id.progressBar);

            Bundle sentData = getArguments();
            final String favorite = sentData.getString("favorite");
            String url = sentData.getString("url");

            if (favorite != null && favorite.contains("favorite")) {
                FavoriteMovies();
            } else {
                new FetchMovies().execute(url);
            }
            gridPosters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String movieId = null ;
                    MovieDBHandler dbHandler = new MovieDBHandler(getContext());
                    Movie mMovie = null ;
                    DBTable table = null ;

                    if (favorite != null && favorite.contains("favorite"))
                    {
                        ArrayList<DBTable> DBList = dbHandler.selectAll();
                        table = DBList.get(position);
                        mMovie = FetchMovieByURI(table.getId());
                    }
                    else
                        mMovie = movieList.get(position);

                    mListener.onFragmentInteraction(mMovie);
                }
            });

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error :" + e, Toast.LENGTH_LONG).show();
        }
        return fragment;
    }

    public void FavoriteMovies()
    {
        MovieDBHandler movieDBHandler = new MovieDBHandler(getContext());
        final ArrayList<DBTable> all = movieDBHandler.selectAll();

        gridPosters.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return all.size();
            }

            @Override
            public Object getItem(int position) {
                return all.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                ImageView ivPoster ;
                if (view == null) {

                    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    view = inflater.inflate(R.layout.grid_list_item,null);

                    ivPoster = (ImageView)view.findViewById(R.id.movie_poster);

                    int h = getContext().getResources().getDisplayMetrics().densityDpi;
//
                    ivPoster.setLayoutParams(new GridView.LayoutParams(h - 45, h - 39));

                    ivPoster.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    view.setTag(ivPoster);
                } else {
                    ivPoster = (ImageView) view.getTag();
                }

                Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + all.get(position).getPoster()).into(ivPoster);

                return view;
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Movie mov);
    }

    public Movie FetchMovieByURI(String id) {

        String url = "http://api.themoviedb.org/3/movie/" + id + "?api_key=" + API_KEY;

        new MyTask(listener).execute(url);

        return listener.getMovie() ;
    }

    class FetchMovies extends AsyncTask<String, Integer, String> {

        private ArrayList<Movie> movies;

        FetchMovies() {
            movies = new ArrayList<Movie>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            movies.clear();
            if (bar != null)
                bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            //      Toast.makeText(getContext(), "doInBackground", Toast.LENGTH_LONG).show();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                String baseUrl = urls[0];

                URL url = new URL(baseUrl);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e("Movies", "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Movies", "Error closing stream", e);
                    }
                }
            }
            return movieJsonStr;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (bar != null)
                bar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            try {

                //    Toast.makeText(getContext(),"HELLLO",Toast.LENGTH_LONG).show();

                super.onPostExecute(response);
                //  TextView test = (TextView) findViewById(R.id.test2);
                JSONObject json = new JSONObject(response);
                JSONArray results = json.getJSONArray("results");


                for (int i = 0; i < results.length(); i++) {
                    Movie temp = new Movie();
                    JSONObject jsonObj = results.getJSONObject(i);
                    temp.setPoster(jsonObj.getString("poster_path"));
                    temp.setId(jsonObj.getString("id"));
                    temp.setTitle(jsonObj.getString("original_title"));
                    temp.setOverview(jsonObj.getString("overview"));
                    temp.setReleaseDate(jsonObj.getString("release_date"));
                    temp.setUserRating(jsonObj.getDouble("vote_average"));

                    movies.add(temp);
                }

                movieList = movies;
                gridPosters.setAdapter(new MovieAdapter(getContext(), movies));

                if (bar != null)
                    bar.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                Log.e("Movies", "Json Error ", e);
            }
        }
    }

    class MyTask extends AsyncTask<String, Integer, String> {

        private OnTaskCompleted listener;

        public MyTask(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                String baseUrl = params[0];

                URL url = new URL(baseUrl);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e("Movies", "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Movies", "Error closing stream", e);
                    }
                }
            }
            return movieJsonStr;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                //super.onPostExecute(response);

                JSONObject jsonObj = new JSONObject(response);

                Movie temp = new Movie();

                temp.setPoster(jsonObj.getString("poster_path"));
                temp.setId(jsonObj.getString("id"));
                temp.setTitle(jsonObj.getString("original_title"));
                temp.setOverview(jsonObj.getString("overview"));
                temp.setReleaseDate(jsonObj.getString("release_date"));
                temp.setUserRating(jsonObj.getDouble("vote_average"));


                movie.clone(temp);
                listener.onTaskCompleted(temp);

            } catch (JSONException e) {
                Log.e("Movies", "Json Error ", e);
            }
        }
    }

}

