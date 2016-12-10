package com.example.islam.movieapp;

import java.io.Serializable;

/**
 * Created by islam on 11/15/2016.
 */

class Movie implements Serializable {

    private String poster_path;
    private String overview;
    private String release_date;
    private String id;
    private String original_title;
    private double vote_average;

    public Movie() {
    }

    public Movie(Movie m) {
        poster_path = m.poster_path;
        overview = m.getOverview();
        release_date = m.getReleaseDate();
        id = m.getId();
        original_title = m.getTitle();
        vote_average = m.getUserRating();
    }

    public void clone(Movie m) {
        this.setPoster(m.getPoster());
        this.setId(m.getId());
        this.setUserRating(m.getUserRating());
        this.setReleaseDate(m.getReleaseDate());
        this.setOverview(m.getOverview());
        this.setTitle(m.getTitle());
    }

    public String getPoster() {
        return this.poster_path;
    }

    public String getOverview() {
        return this.overview;
    }

    public String getReleaseDate() {
        return this.release_date;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.original_title;
    }

    public double getUserRating() {
        return vote_average;
    }

    /////////////////////////////////////
    public void setPoster(String poster) {
        this.poster_path = poster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String date) {
        this.release_date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.original_title = title;
    }

    public void setUserRating(double vote_average) {
        this.vote_average = vote_average;
    }
}
