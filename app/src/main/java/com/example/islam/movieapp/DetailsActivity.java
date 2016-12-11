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

import static com.example.islam.movieapp.R.layout;

/**
 * Created by islam on 11/26/2016.
 */

public class DetailsActivity extends AppCompatActivity {

    public MovieDBHandler movieDBHandler = null;
    ArrayList<Trailer> trailersList;

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        movieDBHandler = new MovieDBHandler(this);

        final String API_KEY = "f938081fe3aeb032354e55c5d152d05f";
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_details);

        try {

            setTitle("Movie Details");

            Intent i = this.getIntent();

            Movie movie = (Movie) i.getSerializableExtra("movie");

            Bundle sentBundle = new Bundle();

            sentBundle.putSerializable("movie", movie);


            DetailsFragment mDetailsFragment = new DetailsFragment();

            mDetailsFragment.setArguments(sentBundle);

            if (savedInstanceState == null) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flDetails, mDetailsFragment, "")
                        .commit();
            }

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

        Intent i = this.getIntent();

        final Movie movie = (Movie) i.getSerializableExtra("movie");

        getMenuInflater().inflate(R.menu.menu_movie_details, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.back:
//                Intent intent = new Intent(DetailsActivity.this,MainActivity.class);
  //              startActivity(intent);
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}





