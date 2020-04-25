package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {
    private String poster = null;
    public static final String EXTRA_MESSAGE = "com.example.movilebuffs.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        String imdb = intent.getStringExtra(MainActivity.EXTRA_MESSAGE); //get imdb from selected movie

        //set up variables for textviews
        final TextView movieDetails = (TextView) findViewById(R.id.textViewDetails);

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
                            //JSONArray jsonList = response.getJSONArray("Search");
                            title = response.getString("Title");
                            year = response.getString("Year");
                            genre = response.getString("Genre");
                            director = response.getString("Director");
                            writer = response.getString("Writer");
                            plot = response.getString("Plot");
                            poster = response.getString("Poster");

                            String detailsString = "Title: " + title + "\n" +
                                    "Year: " + year + "\n" +
                                    "Genre: " + genre + "\n" +
                                    "Director: " + director + "\n" +
                                    "Writer: " + writer + "\n" +
                                    "Plot: " + plot;
                            //detailsString = detailsString.replace("\\\n", System.getProperty("line.separator"));
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

    }

    /**
     * Called when the user taps the View Poster button
     * Navigates to MoviePosterActivity with the selected Movie
     * */
    public void viewPoster(View view) {
        if(poster != null) {
            Intent intent = new Intent(this, MoviePosterActivity.class);
            intent.putExtra(EXTRA_MESSAGE, poster);
            startActivity(intent);
        }
        else{
            Toast.makeText(MovieDetailActivity.this, "Search Failed!", Toast.LENGTH_LONG).show();
        }
    }
}
