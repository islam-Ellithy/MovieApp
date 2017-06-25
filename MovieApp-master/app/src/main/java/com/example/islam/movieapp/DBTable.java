package com.example.islam.movieapp;

import java.io.Serializable;

/**
 * Created by islam on 12/9/2016.
 */

public class DBTable implements Serializable{
    private String Poster ;
    private String Id ;

    public String getPoster()
    {
        return this.Poster ;
    }
    public String getId()
    {
        return this.Id ;
    }

    public void setPoster(String poster)
    {
        this.Poster = poster ;
    }
    public void setId(String id)
    {
        this.Id = id ;
    }
}
