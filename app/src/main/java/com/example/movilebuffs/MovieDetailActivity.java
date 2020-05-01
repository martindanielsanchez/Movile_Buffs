package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    private String poster = null;
    public static final String POSTER = "com.example.movilebuffs.POSTER";
    public static final String MOVIE = "com.example.movilebuffs.MOVIE";
    private String userJSON = null;
    private Movie film = null;
    private Boolean isFavorite = false;
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_add_favorite = "http://192.168.135.1:80/android_connect/add_favorite.php";
    private static String url_remove_favorite = "http://192.168.135.1:80/android_connect/remove_favorite.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private User logged;
    private String imdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        //final String imdb = intent.getStringExtra(MovieListActivity.IMDB); //get imdb from selected movie
        imdb = intent.getStringExtra(MovieListActivity.IMDB); //get imdb from selected movie
        userJSON = intent.getStringExtra(MainActivity.USER); //get user json string
        //set up variables for textviews
        final TextView movieDetails = (TextView) findViewById(R.id.textViewDetails);

        //first check if movie is on favorites list
        User logged = new Gson().fromJson(userJSON, User.class);
        //isFavorite = logged.isMovieFavorite(film);
        ArrayList<Movie> favorites = logged.getFavorites();
       // Boolean flag = false;
        if(favorites.isEmpty()){
            isFavorite = false;
        }
        else{
            for (int i = 0; i < favorites.size(); i++) { //traverses each movie in list
                if(favorites.get(i).getImdb().equals(imdb)){
                    isFavorite = true;
                }
            }
        }

        //set button text accordingly
        Button favButton = (Button) findViewById(R.id.button3);
        if(isFavorite){
            favButton.setText("Remove from Favorites");
            //Toast.makeText(MovieDetailActivity.this, "Inside Remove from Favorites", Toast.LENGTH_LONG).show();
        }
        else{
            favButton.setText("Add to Favorites");
           // Toast.makeText(MovieDetailActivity.this, "Inside Add to Favorites. isFavorite = " + isFavorite, Toast.LENGTH_LONG).show();
        }


        //make API CALL based on imdb code
        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(MovieDetailActivity.this.getApplicationContext()).
                getRequestQueue();
        String url = "http://www.omdbapi.com/?i=" + imdb + "&apikey=e7272e5d";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String title = null;
                       // String imdb = null;
                        String year = null;
                        String genre = null;
                        String director = null;
                        String writer = null;
                        String plot = null;


                        //title = response.getString("name");

                        try {
                            title = response.getString("Title");
                            year = response.getString("Year");
                            genre = response.getString("Genre");
                            director = response.getString("Director");
                            writer = response.getString("Writer");
                            plot = response.getString("Plot");
                            poster = response.getString("Poster");

                            film = new Movie(title, year, genre, director, writer, plot, imdb);

                            String detailsString = "Title: " + title + "\n" +
                                    "Year: " + year + "\n" +
                                    "Genre: " + genre + "\n" +
                                    "Director: " + director + "\n" +
                                    "Writer: " + writer + "\n" +
                                    "Plot: " + plot;
                            movieDetails.setText(detailsString);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO: Handle error
                        Toast.makeText(MovieDetailActivity.this, "Search Failed!", Toast.LENGTH_LONG).show();

                    }
                });
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(MovieDetailActivity.this).addToRequestQueue(jsonObjectRequest);
/*
        //check if movie is in favorites list
        User logged = new Gson().fromJson(userJSON, User.class);
        isFavorite = logged.isMovieFavorite(film);
        //set button text accordingly
        Button favButton = (Button) findViewById(R.id.button3);
        if(isFavorite){
            favButton.setText("Remove from Favorites");
            Toast.makeText(MovieDetailActivity.this, "Inside Remove from Favorites", Toast.LENGTH_LONG).show();
        }
        else{
            favButton.setText("Add to Favorites");
            Toast.makeText(MovieDetailActivity.this, "Inside Add to Favorites. isFavorite = " + isFavorite, Toast.LENGTH_LONG).show();
        }*/
    }

    /**
     * Called when the user taps the View Poster button
     * Navigates to MoviePosterActivity with the selected Movie
     * */
    public void viewPoster(View view) {
        if(poster != null) {
            String filmJSON = (new Gson().toJson(film));
            Intent intent = new Intent(this, MoviePosterActivity.class);
            intent.putExtra(MainActivity.USER, userJSON); //send USER
            intent.putExtra(POSTER, poster);
            intent.putExtra(MOVIE, filmJSON);
            startActivity(intent);
        }
        else{
            Toast.makeText(MovieDetailActivity.this, "Search Failed!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * OnClick Handler, decides which function to call depending on whether the Movie is on favorites list or not.
     */
    public void favoritesHandler (View view){
        if(isFavorite){
            this.removeFromFavorites(view); //function to remove from favorites list
        }
        else{
            addToFavorites(view); //function to add to favorites
        }
    }

    /**
     * Called when the user taps the Add to Favorites Button
     * Adds the selected movie to the user's favorites list
     */
    public void addToFavorites(View view){
        logged = new Gson().fromJson(userJSON, User.class); //get User from JSON string

        //first check if movie is already in list
        if(!logged.isMovieFavorite(film)) {
           // Toast.makeText(MovieDetailActivity.this, "Movie not favorite, returned false", Toast.LENGTH_LONG).show();

            new AddFavorite().execute(); //updates database
            logged.addFavorite(film); //updates object used in app

        }
        else{
            Toast.makeText(MovieDetailActivity.this, "Movie is favorite, returned true", Toast.LENGTH_LONG).show();
        }
        //Send user back to MovieListActivity
        Intent intent = new Intent(this, MovieListActivity.class);
        userJSON = (new Gson().toJson(logged)); //update user json string to contain new list of favorites
        intent.putExtra(MainActivity.USER, userJSON); //send USER
        startActivity(intent);
    }

    /**
     * Called when the user taps the Remove from Favorites Button
     * Removes the selected movie from the user's favorites list
     */
    public void removeFromFavorites(View view){
        logged = new Gson().fromJson(userJSON, User.class); //get User from JSON string
        //first check if movie is effectively in list
        if(logged.isMovieFavorite(film)) {
            new RemoveFavorite().execute(); //updates database
            logged.removeFromFavorites(film);
        }
        //Send user back to MovieListActivity
        Intent intent = new Intent(this, MovieListActivity.class);
        userJSON = (new Gson().toJson(logged)); //update user json string to contain new list of favorites
        intent.putExtra(MainActivity.USER, userJSON); //send USER
        startActivity(intent);
    }

    /**
     * Background Async Task to Add new favorite movie
     * */
    class AddFavorite extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MovieDetailActivity.this);
            pDialog.setMessage("Adding Favorite..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Adding to favorites
         * */
        protected String doInBackground(String... args) {

            String username = logged.getUsername();
            //String imdb = film.getImdb();
            String title = film.getTitle();

            // Building Parameters
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("username", username));
            params1.add(new BasicNameValuePair("imdb", imdb));
            params1.add(new BasicNameValuePair("title", title));
            // getting JSON Object
            // Note that add_favorite url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(url_add_favorite,
                    "POST", params1);

            // check log cat fro response
            Log.d("Create Response", json1.toString());

            // check for success tag
            try {
                int success1 = json1.getInt(TAG_SUCCESS);

                if (success1 == 1) {
                    // favorite successfully added

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Successfully added Favorite", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    // Favorite was not added
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Could not add favorite. Check Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

    /**
     * Background Async Task to Remove movie from favorite table in database
     * */
    class RemoveFavorite extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MovieDetailActivity.this);
            pDialog.setMessage("Removing Favorite..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Removing from favorites
         * */
        protected String doInBackground(String... args) {

            String username = logged.getUsername();
           // String title = film.getTitle();

            // Building Parameters
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("username", username));
            params1.add(new BasicNameValuePair("imdb", imdb));
            //params1.add(new BasicNameValuePair("title", title));
            // getting JSON Object
            // Note that add_favorite url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(url_remove_favorite,
                    "POST", params1);

            // check log cat fro response
            Log.d("Create Response", json1.toString());

            // check for success tag
            try {
                int success1 = json1.getInt(TAG_SUCCESS);

                if (success1 == 1) {
                    // favorite successfully removed

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Successfully removed Favorite", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    // Favorite was not added
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Could not remove favorite. Check Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
