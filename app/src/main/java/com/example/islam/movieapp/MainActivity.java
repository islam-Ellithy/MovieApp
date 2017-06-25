package com.example.islam.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    static String API_KEY = "f938081fe3aeb032354e55c5d152d05f";
    private ArrayList<Movie> movies;
    private Context context;
    Boolean IsTwoPane = false;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override

    protected void onPostResume() {
        super.onPostResume();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IsTwoPane = false;
        setTitle("Pop Movies");
        String url = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;

        if (isNetworkAvailable()) {
            if (savedInstanceState == null) {


                MainFragment mainFragment = new MainFragment();

                mainFragment.setmListener(this);

                Bundle extra = new Bundle();

                extra.putString("url", url);

                mainFragment.setArguments(extra);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flMain, mainFragment, "")
                        .commit();

                if (findViewById(R.id.flDetails) != null) {
                    IsTwoPane = true;
                }
            }
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onFragmentInteraction(Movie mov) {

        //Case One Pane
        //Start Details Activity
        if (isNetworkAvailable()) {

            if (!IsTwoPane) {
                Intent i = new Intent(this, DetailsActivity.class);
                if (mov.getId() != null) {
                    i.putExtra("movie", mov);
                    startActivity(i);
                }
            } else {//Case Two Pane
                DetailsFragment detailsFragment = new DetailsFragment();
                Bundle extras = new Bundle();

                if (mov.getId() != null) {

                    extras.putSerializable("movie", mov);
                    detailsFragment.setArguments(extras);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flDetails, detailsFragment, "")
                            .commit();
                }
            }
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    //check internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MainFragment m = new MainFragment();
        m.setmListener(this);
        int id = item.getItemId();
        String url = null;
        Bundle bundle = new Bundle();
        item.setChecked(true);

        try {
            if (isNetworkAvailable()) {

                if (id == R.id.most_popular) {

                    url = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
                    bundle.putString("url", url);

                    m.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flMain, m, "")
                            .commit();

                    return true;
                }
                if (id == R.id.top_rated) {

                    url = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
                    bundle.putString("url", url);
                    m.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flMain, m, "")
                            .commit();
                    return true;
                }
                if (id == R.id.favorite) {
                    String favorite = "favorite";
                    bundle.putString("favorite", favorite);
                    m.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flMain, m, "")
                            .commit();
                    return true;
                }
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

}
