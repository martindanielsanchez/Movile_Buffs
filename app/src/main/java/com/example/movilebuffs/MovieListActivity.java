package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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

        final ScrollView list = (ScrollView) findViewById(R.id.scrollViewList);
        EditText ed1 = (EditText) findViewById(R.id.editText);
        String searchString = ed1.getText().toString();
        //final TextView tv1 = new TextView(this);
        //tv1.setText("TESTING");

       // list.addView(tv1);
        //Toast.makeText(getApplicationContext(), "testing", Toast.LENGTH_SHORT).show();




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

                            title = response.toString();
                            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();

                            /*try {
                               // title = response.getJSONObject("Search").getString("title");
                                title = response.toString();
                                //cityName = response.getString("name");
                               // wind = response.getJSONObject("wind").getString("speed");
                                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
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


    }
}
