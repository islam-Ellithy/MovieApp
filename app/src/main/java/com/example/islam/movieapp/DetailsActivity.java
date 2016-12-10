package com.example.islam.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.islam.movieapp.R.id;
import static com.example.islam.movieapp.R.layout;

/**
 * Created by islam on 11/26/2016.
 */

public class DetailsActivity extends AppCompatActivity {

    public MovieDBHandler movieDBHandler = null;
    ArrayList<Trailer> trailersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        movieDBHandler = new MovieDBHandler(this);

        final String API_KEY = "f938081fe3aeb032354e55c5d152d05f";
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_details);

        try {

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

           //PrintTable();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent i = this.getIntent();

        final Movie movie = (Movie) i.getSerializableExtra("movie");


        getMenuInflater().inflate(R.menu.menu_movie_details, menu);

        MenuItem itemView = (MenuItem) menu.findItem(id.action_favorite);

        if (movieDBHandler.isChecked(movie.getId())) {
            itemView.setChecked(true);
            itemView.setIcon(R.drawable.on);
        } else {
            itemView.setChecked(false);
            itemView.setIcon(R.drawable.off);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = this.getIntent();
        final Movie movie = (Movie) i.getSerializableExtra("movie");
        try {

            switch (item.getItemId()) {
                //favorite item if selected
                case id.action_favorite:
                    if (item.isChecked()) {
                        item.setChecked(false);
                        item.setIcon(R.drawable.off);
                        if (movie.getId() != null) {
                            movieDBHandler.deleteMovie(movie.getId());
                        }
                        Toast.makeText(getBaseContext(),"Marked as non favorite",Toast.LENGTH_SHORT).show();

                    } else {
                        DBTable t = new DBTable();
                        t.setPoster(movie.getPoster());
                        t.setId(movie.getId());
                        item.setChecked(true);
                        item.setIcon(R.drawable.on);
                        if (movie.getId() != null) {
                            movieDBHandler.addMovie(t);
                        }
                        Toast.makeText(getBaseContext(),"Marked as favorite",Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case id.back:
                    finish();
                    return true;
                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            // Toast.makeText(this,"exception Due to :" +e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return true;
    }


}





