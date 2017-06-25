package com.example.islam.movieapp;

/**
 * Created by islam on 12/4/2016.
 */
class Review {
    private String author;
    private String content;
    private String url;

    ////////////////// getters
    public String getAuthor() {
        return this.author;
    }

    public String getContent() {
        return this.content;
    }

    public String getUrl() {
        return this.url;
    }

    /////////////////////setters
    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
