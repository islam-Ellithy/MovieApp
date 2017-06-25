package com.example.islam.movieapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.islam.movieapp.R.drawable.on;



//    private OnFragmentInteractionListener mListener;

public class DetailsFragment extends Fragment {

    static String API_KEY = "f938081fe3aeb032354e55c5d152d05f";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<Trailer> trailersList;
    public MovieDBHandler movieDBHandler = null;
    ImageView img = null;
    TextView title = null;
    TextView overview = null;
    TextView date = null;
    TextView avg = null;
    TextView test = null;
    ImageButton favorite = null;
    boolean Isfavorite = true;
    Movie movie ;
    public DetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();

        final Movie moviee = (Movie) bundle.getSerializable("movie");

        this.movie = moviee ;

        AsyncTask<Movie, Integer, Movie> asyncTask = new AsyncTask<Movie, Integer, Movie>() {

            @Override
            protected Movie doInBackground(Movie... params) {

                return params[0];
            }

            @Override
            protected void onPostExecute(Movie s) {

                Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + movie.getPoster()).into(img);

                title.setText(movie.getTitle());

                overview.setText(movie.getOverview());

                overview.setMovementMethod(new ScrollingMovementMethod());

                date.setText(movie.getReleaseDate());

                avg.setText(movie.getUserRating() + "/10");

                trailersList = new ArrayList<Trailer>();

                FetchTrailersAndReviews(movie.getId());

            }
        };

        asyncTask.execute(movie);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View detailsFragment = inflater.inflate(R.layout.fragment_details, container, false);

        try {

            movieDBHandler = new MovieDBHandler(getContext());
            img = (ImageView) detailsFragment.findViewById(R.id.movieImage);
            title = (TextView) detailsFragment.findViewById(R.id.title);
            overview = (TextView) detailsFragment.findViewById(R.id.overview);
            date = (TextView) detailsFragment.findViewById(R.id.date);
            avg = (TextView) detailsFragment.findViewById(R.id.rate);
            favorite = (ImageButton) detailsFragment.findViewById(R.id.favorite);


            final MovieDBHandler dbHandler = new MovieDBHandler(getContext());

            if (dbHandler.isChecked(movie.getId())) {
                favorite.setImageResource(on);
                Isfavorite = true;
            } else {
                favorite.setImageResource(R.drawable.off);
                Isfavorite = false;
            }


            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (dbHandler.isChecked(movie.getId())) {
                        dbHandler.deleteMovie(movie.getId());
                        favorite.setImageResource(R.drawable.off);
                        Isfavorite = false;
                    } else {
                        DBTable d = new DBTable();
                        d.setId(movie.getId());
                        d.setPoster(movie.getPoster());
                        dbHandler.addMovie(d);
                        favorite.setImageResource(R.drawable.on);
                        Isfavorite = true;
                    }
                }
            });



        } catch (Exception e) {
           // Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return detailsFragment;
    }


    private void FetchTrailersAndReviews(String id) {
        new FetchTrailer(getActivity()).execute("http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + API_KEY);
        new FetchReviews(getActivity()).execute("http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + API_KEY);
    }


}