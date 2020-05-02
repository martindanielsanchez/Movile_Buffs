package com.example.movilebuffs;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private ArrayList<Movie> favorites;

    User(String first, String last, String user, ArrayList<Movie> fav){
        firstName = first;
        lastName = last;
        username = user;
       // password = pass;
        favorites = fav;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public ArrayList<Movie> getFavorites(){
        return favorites;
    }

    public String getUsername(){ return username;}

    /**
     * Function to add a new movie to list of favorite movies
     * @param favMovie is a movie to be added to list
     */
    public void addFavorite(Movie favMovie){
        favorites.add(favMovie);
    }

    /**
     * Function to check if movie is in favorites list
     * @param film Movie that will be searched for in user's favorites list
     * @return true if movie is on list, false if not
     */
    public Boolean isMovieFavorite(Movie film){
        if(favorites.isEmpty()){
            return false;
        }
        else{
            for (int i = 0; i < favorites.size(); i++) { //traverses each movie in list
                if(favorites.get(i).getImdb().equals(film.getImdb())){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Function to remove movie from favorites list
     * @param film is the Movie object that will be removed from list if found
     * Prerequisite: Movie must be in list
     */
    public void removeFromFavorites(Movie film){
        if(favorites.isEmpty()){
            return;
        }
        else{
            for (int i = 0; i < favorites.size(); i++) { //traverses each movie in list
                if(favorites.get(i).getImdb().equals(film.getImdb())){
                    favorites.remove(i);
                }
            }
        }
    }
}
