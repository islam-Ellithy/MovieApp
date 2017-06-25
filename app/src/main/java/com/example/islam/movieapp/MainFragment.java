package com.example.islam.movieapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

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
<<<<<<< HEAD

=======
>>>>>>> e6729961d5a8f8aeeadbb72e06ad8c928a680410
        @Override
        public void onTaskCompleted(Movie mMovie) {
            movie.clone(mMovie);
        }

        @Override
        public Movie getMovie() {
            return movie;
        }
    };

    public MainFragment() {
        movieList = new ArrayList<Movie>();
    }


    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onResume() {
        super.onResume();
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

                    Movie mMovie = movieList.get(position);

                    mListener.onFragmentInteraction(mMovie);
                }
            });

        } catch (Exception e) {
            //Toast.makeText(getContext(), "Error :" + e, Toast.LENGTH_LONG).show();
        }
        return fragment;
    }


    public void FavoriteMovies() {
        FetchFavoriteMovies();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Movie mov);
    }

    public void FetchFavoriteMovies() {

        MovieDBHandler dbhandler = new MovieDBHandler(getContext());
        ArrayList<DBTable> allTable = dbhandler.selectAll();

        new MyTask(listener, allTable).execute();

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

                super.onPostExecute(response);
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
                    temp.setUserRating(jsonObj.getString("vote_average"));

                    movies.add(temp);
                }

<<<<<<< HEAD

=======
>>>>>>> e6729961d5a8f8aeeadbb72e06ad8c928a680410
                movieList = movies;

                gridPosters.setAdapter(new MovieAdapter(getContext(), movies));

                if (bar != null)
                    bar.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                Log.e("Movies", "Json Error ", e);
            }
        }
    }

    class MyTask extends AsyncTask<Void, Integer, ArrayList<String>> {

        private OnTaskCompleted listener;
        ArrayList<DBTable> dbTables;

        public MyTask(OnTaskCompleted listener, ArrayList<DBTable> dbTables) {
            this.listener = listener;
            this.dbTables = dbTables;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            ArrayList<String> jsonArr = new ArrayList<String>();


            try {

                for (int i = 0; i < dbTables.size(); i++) {

                    String id = dbTables.get(i).getId();
                    String baseUrl = "http://api.themoviedb.org/3/movie/" + id + "?api_key=" + API_KEY;

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

                    jsonArr.add(movieJsonStr);
                }

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
            return jsonArr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            movieList.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<String> response) {
            try {

                super.onPostExecute(response);

                ArrayList<Movie> movieArr = new ArrayList<Movie>();
                for (String json : response) {

                    JSONObject jsonObj = new JSONObject(json);

                    Movie temp = new Movie();

                    temp.setPoster(jsonObj.getString("poster_path"));
                    temp.setId(jsonObj.getString("id"));
                    temp.setTitle(jsonObj.getString("original_title"));
                    temp.setOverview(jsonObj.getString("overview"));
                    temp.setReleaseDate(jsonObj.getString("release_date"));
                    temp.setUserRating(jsonObj.getString("vote_average"));

                    listener.onTaskCompleted(temp);

                    movieArr.add(temp);

                }

                movieList = movieArr;
                gridPosters.setAdapter(new MovieAdapter(getContext(), movieArr));

            } catch (JSONException e) {
                Log.e("Movies", "Json Error ", e);
            }
        }
    }

}

