package com.example.islam.movieapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by islam on 12/8/2016.
 */


class FetchTrailer extends AsyncTask<String, String, String> {

    private RecyclerView.LayoutManager mLayoutManager = null ;
    private RecyclerView mRecyclerView = null;
    private RecyclerView.Adapter mAdapter = null;
    public ArrayList<Trailer> trailersList;
    public Context context;
    private Activity activity;

    public FetchTrailer(ArrayList<Trailer> tr, Context c) {
        trailersList = new ArrayList<Trailer>(tr);
        context = c;
    }

    public FetchTrailer(Activity activity) {

        this.trailersList = new ArrayList<Trailer>();
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... urls) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailerJsonStr = null;

        try {

            URL url = new URL(urls[0]);

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
            trailerJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("Trailers", "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Trailers", "Error closing stream", e);
                }
            }
        }
        return trailerJsonStr;
    }

    @Override
    protected void onPreExecute() {
        trailersList.clear();
    }

    @Override
    protected void onPostExecute(String trailerJsonStr) {

        try {
            if (trailerJsonStr != null) {

                JSONObject jsonObject = new JSONObject(trailerJsonStr);
                JSONArray results = jsonObject.getJSONArray("results");
                Trailer temp;
                for (int i = 0; i < results.length(); i++) {
                    temp = new Trailer();
                    JSONObject j = results.getJSONObject(i);
                    temp.setKey(j.getString("key"));
                    temp.setName(j.getString("name"));
                    trailersList.add(temp);
                }

                mRecyclerView = (RecyclerView) activity.findViewById(R.id.traillers);
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(activity.getBaseContext());

                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new TrailerAdapter(trailersList,activity);

                mRecyclerView.setAdapter(mAdapter);

            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("Trailers", "Json Error ", e);
        }

    }

    @Override
    protected void onProgressUpdate(String... values) {
    }
}


class FetchReviews extends AsyncTask<String, String, String> {

    ArrayList<Review> reviewsList ;
    Activity activity ;
    public FetchReviews(Activity activity) {

        this.reviewsList = new ArrayList<Review>();
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }



    @Override
    protected String doInBackground(String... urls) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String reviewsJsonStr = null;

        try {

            URL url = new URL(urls[0]);

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
            reviewsJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("Reviews", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Reviews", "Error closing stream", e);
                }
            }
        }
        return reviewsJsonStr;
    }


    @Override
    protected void onPostExecute(String response) {
        ArrayList<Review> reviews = new ArrayList<Review>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            Review r = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                r = new Review();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                r.setAuthor(jsonObject1.getString("author"));
                r.setContent(jsonObject1.getString("content"));
                r.setUrl(jsonObject1.getString("url"));
                reviews.add(r);
            }

            RecyclerView.Adapter mAdapter = null;
            RecyclerView.LayoutManager mLayoutManager = null;
            RecyclerView mRecyclerView = (RecyclerView) activity.findViewById(R.id.reviews);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(activity.getBaseContext());

            mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerView.setAdapter(new ReviewAdapter(reviews,activity));

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        } catch (JSONException e) {
            Log.e("Reviews", "Json Error ", e);
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
    }

}

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    ArrayList<Trailer> trailers;
    private Activity activity;

    public TrailerAdapter(ArrayList<Trailer> t , Activity activity)
    {
        this.trailers = t;
        this.activity = activity ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView logo;
        public TextView title;

        public ViewHolder(View layout) {
            super(layout);
            this.logo = (ImageView) layout.findViewById(R.id.play_logo);
            this.title = (TextView) layout.findViewById(R.id.trailer_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_details, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            holder.title.setText(trailers.get(position).name);
            holder.logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://www.youtube.com/watch?v=" + trailers.get(position).getKey();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                }
            });
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

}
class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    ArrayList<Review> reviews;
    private Activity activity;

    ReviewAdapter(ArrayList<Review> r,Activity activity) {
        reviews = r;
        this.activity = activity ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;

        public ViewHolder(View v) {
            super(v);
            author = (TextView) v.findViewById(R.id.author);
            content = (TextView) v.findViewById(R.id.content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_details, parent, false);

        ViewHolder viewHolder = new ViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.author.append(" " + reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
