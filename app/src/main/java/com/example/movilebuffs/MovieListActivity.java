package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String s = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        User logged = new Gson().fromJson(s, User.class);
        final TextView textView =  findViewById(R.id.textViewUser);
        textView.setText("Hello " + logged.getFirstName());
    }




    /**
     * Called when the user taps the Search button
     * Populates ScrollView with movies that match search criteria
     * */
    public void searchMovies(View view) {
        //Intent intent = new Intent(this, MoviePosterActivity.class);
        //startActivity(intent);

       // final ScrollView list = (ScrollView) findViewById(R.id.scrollViewList);
        EditText ed1 = (EditText) findViewById(R.id.editText);
        String searchString = ed1.getText().toString();

        //set up listview
        final ListView list = (ListView) findViewById(R.id.movieList);
        final ArrayList<String> arrayList = new ArrayList<>();

/*
        arrayList.add("MOVIE 1");
        arrayList.add("MOVIE 2");
        arrayList.add("MOVIE 3");
        arrayList.add("MOVIE 4");
        arrayList.add("MOVIE 5");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);*/

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(MovieListActivity.this.getApplicationContext()).
                getRequestQueue();
       // String url = "https://api.openweathermap.org/data/2.5/weather?zip=33498&appid=26182d24dcc44f587cb7bba6786bd237";
        String url = "http://www.omdbapi.com/?s=" + searchString + "&apikey=e7272e5d";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            String title = null;
                            String temperature = null;
                            String wind = null;
                            String pressure = null;
                            String humidity = null;
                            String sunrise = null;
                            //title = response.toString();

                            //tv1.setText(title);

                          //  list.addView(tv1);
                            //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();

                            //title = response.toString();
                            //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                            //JSONArray jsonList = response.getJSONArray("Search");

                            try {
                                JSONArray jsonList = response.getJSONArray("Search");
                                //Toast.makeText(getApplicationContext(), jsonList.toString(), Toast.LENGTH_LONG).show();
                               // ArrayList<String> list = new ArrayList<String>();
                                String sTest = null;
                                //ArrayList<String> arrayList2 = new ArrayList<>();

                                for (int i = 0; i < jsonList.length(); i++) { //traverses each result from search
                                 //   list.add(jsonList.getString(i));
                                    title = jsonList.getJSONObject(i).getString("Title");
                                   // title = jsonList.getJSONObject(i).toString();
                                    //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
                                    //arrayList.add(title);
                                    arrayList.add(title);
                                    //sTest += jsonList.getString(i);
                                }
                                // Add list to view
                                ArrayAdapter arrayAdapter = new ArrayAdapter(MovieListActivity.this,android.R.layout.simple_list_item_1, arrayList);
                                list.setAdapter(arrayAdapter);


                               // Toast.makeText(getApplicationContext(), sTest, Toast.LENGTH_LONG).show();

                                // title = response.getJSONObject("Search").getString("title");
                                //title = response.toString();
                                //cityName = response.getString("name");
                               // wind = response.getJSONObject("wind").getString("speed");
                                //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();



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
                            //textView.setText("That didn't work!");

                        }
                    });
// Access the RequestQueue through your singleton class.
            MySingleton.getInstance(MovieListActivity.this).addToRequestQueue(jsonObjectRequest);



// Add list to view
       //ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayList);
       //list.setAdapter(arrayAdapter);

    }
}
