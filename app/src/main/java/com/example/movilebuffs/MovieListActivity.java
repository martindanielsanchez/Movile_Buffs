package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MovieListActivity extends AppCompatActivity {
    public static final String IMDB = "com.example.movilebuffs.IMDB";
    public static final String USER = "com.example.movilebuffs.USER";
    private String userJSON = null;
    private User logged = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        userJSON = intent.getStringExtra(MainActivity.USER);
        logged = new Gson().fromJson(userJSON, User.class);
        final TextView textView =  findViewById(R.id.textViewUser);
        textView.setText("Hello " + logged.getFirstName());
    }

    /**
     * Called when the user taps the View Favorites button
     * Populates ScrollView with User's favorite movies
     * */
    public void viewFavorites(View view){
        final Intent intent = new Intent(this, MovieDetailActivity.class);
        //set up listview
        final ListView list = (ListView) findViewById(R.id.movieList);
        final ArrayList<String> arrayList = new ArrayList<>();

        final ArrayList<String> imdbList = new ArrayList<String>();
        ArrayList<Movie> favList = logged.getFavorites();
        if(!favList.isEmpty()) {
            for (int i = 0; i < favList.size(); i++) { //traverses each movie
                String title = favList.get(i).getTitle();
                String imdb = favList.get(i).getImdb();
                imdbList.add(imdb);
                arrayList.add(title); //add to list that will be displayed in ListView
            }
            // Add list to view
            ArrayAdapter arrayAdapter = new ArrayAdapter(MovieListActivity.this, android.R.layout.simple_list_item_1, arrayList);
            list.setAdapter(arrayAdapter);

            //add event listener to each item in list
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MovieListActivity.this, "clicked item " + position + " with imdb " + imdbList.get(position) + " " + arrayList.get(position), Toast.LENGTH_LONG).show();
                    //go to MovieDetailActivity and send the imdb code to make another API CALL from there
                    intent.putExtra(USER, userJSON); //also send USER
                    intent.putExtra(IMDB, imdbList.get(position));
                    startActivity(intent);
                }
            });
        }
        else{
            //alert user list is empty
            Toast.makeText(MovieListActivity.this, "Favorites List is empty!", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Called when the user taps the Search button
     * Populates ScrollView with movies that match search criteria
     * */
    public void searchMovies(View view) {
        final Intent intent = new Intent(this, MovieDetailActivity.class);
        //startActivity(intent);

       // final ScrollView list = (ScrollView) findViewById(R.id.scrollViewList);
        EditText ed1 = (EditText) findViewById(R.id.editText);
        String searchString = ed1.getText().toString();

        //set up listview
        final ListView list = (ListView) findViewById(R.id.movieList);
        final ArrayList<String> arrayList = new ArrayList<>();

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(MovieListActivity.this.getApplicationContext()).
                getRequestQueue();
        String url = "http://www.omdbapi.com/?s=" + searchString + "&apikey=e7272e5d";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //create empty list of movies
                            ArrayList <Movie> movies = new ArrayList<>();
                            String title = null;
                            String imdb = null;

                            try {
                                JSONArray jsonList = response.getJSONArray("Search");
                               final ArrayList<String> imdbList = new ArrayList<String>();

                                for (int i = 0; i < jsonList.length(); i++) { //traverses each result from search
                                    title = jsonList.getJSONObject(i).getString("Title");
                                    imdb = jsonList.getJSONObject(i).getString("imdbID");
                                    imdbList.add(imdb);
                                    arrayList.add(title); //add to list that will be displayed in ListView
                                }
                                // Add list to view
                                ArrayAdapter arrayAdapter = new ArrayAdapter(MovieListActivity.this,android.R.layout.simple_list_item_1, arrayList);
                                list.setAdapter(arrayAdapter);

                                //add event listener to each item in list
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Toast.makeText(MovieListActivity.this, "clicked item " + position + " with imdb " + imdbList.get(position) + " " + arrayList.get(position), Toast.LENGTH_LONG).show();
                                        //go to MovieDetailActivity and send the imdb code to make another API CALL from there
                                        intent.putExtra(USER, userJSON); //also send USER
                                        intent.putExtra(IMDB, imdbList.get(position));
                                        startActivity(intent);
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //textView.setText("City: " + cityName + '\n' + "Temperature: " + temperature + '\n' + "Wind: " + wind + '\n' + "Pressure: " + pressure + '\n' +
                            //        "Humidity: " + humidity + '\n' + "Sunrise: " + sunrise);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //TODO: Handle error
                            Toast.makeText(MovieListActivity.this, "Search Failed!", Toast.LENGTH_LONG).show();

                        }
                    });
// Access the RequestQueue through your singleton class.
            MySingleton.getInstance(MovieListActivity.this).addToRequestQueue(jsonObjectRequest);


    }
}
