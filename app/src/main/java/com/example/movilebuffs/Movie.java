package com.example.movilebuffs;

public class Movie {
    private String title = null;
    private String year = null;
    private String genre = null;
    private String director = null;
    private String writer = null;
    private String plot = null;
    private String imdb = null;


    Movie(String t, String y, String g, String d, String w, String p, String i){
        title = t;
        year = y;
        genre = g;
        director = d;
        writer = w;
        plot = p;
        imdb = i;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getGenre(){
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getPlot() {
        return plot;
    }

    public String getImdb() {
        return imdb;
    }
}
