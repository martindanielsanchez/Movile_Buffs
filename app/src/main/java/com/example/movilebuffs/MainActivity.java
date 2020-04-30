package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String USER = "com.example.movilebuffs.USER";
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    // url to create new product
    private static String url_check_pass = "http://192.168.135.1:80/android_connect/check_pass.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FIRST = "first";
    private static final String TAG_LAST = "last";
    private static final String TAG_FAVORITES = "favorites";
    private static final String TAG_FAVSUCCESS = "favSuccess";
    private static final String TAG_TITLE = "title";
    private static final String TAG_IMDB = "imdb";

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Log In button
     * Validates Log In with Database, creates User object and navigates to MovieListActivity
     * */
    public void logIn(View view) {
        //Intent intent = new Intent(this, MovieListActivity.class);
        EditText ed1 = (EditText) findViewById(R.id.editTextUser);
        EditText ed2 = (EditText) findViewById(R.id.editTextPass);

        username = ed1.getText().toString();
        password = ed2.getText().toString();

        //check if username is in database and retrieve it, if not alert user

        new CheckPassword().execute();


/*
        User logUser = new User("Valentino", "Rossi", "vale46", "123", new ArrayList<Movie>()); //CREATING USER FOR TESTING

        Boolean validate = logUser.validateLogIn(pass);
        if(validate){
            String s = (new Gson().toJson(logUser)); //SERIALIZING OBJECT TO SEND IT TO NEXT ACTIVITY
            intent.putExtra(USER, s);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
        }*/
    }

    /**
     * Called when the user taps the Sign Up button
     * Navigates to SignUpActivity
     * */
    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    /**
     * Background Async Task to Create new product
     * */
    class CheckPassword extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Checking credentials..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            //First check if username is already taken by calling webservice that in turn calls a stored procedure
            // Building Parameters
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("username", username));
            params1.add(new BasicNameValuePair("password", password));
            // getting JSON Object
            // Note that check username url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(url_check_pass,
                    "POST", params1);

            // check log cat fro response
            Log.d("Create Response", json1.toString());

            // check for success tag
            try {
                int success1 = json1.getInt(TAG_SUCCESS);

                if (success1 == 1) {
                    // There is no user with that username
                    //Get user info
                    String firstName = json1.getString(TAG_FIRST);
                    String lastName = json1.getString(TAG_LAST);

                    //Check if there are any favorites
                    int favSuccess = json1.getInt(TAG_FAVSUCCESS);

                    ArrayList<Movie> favoriteMovies = new ArrayList<Movie>();

                    if(favSuccess == 1){
                        JSONArray favs = json1.getJSONArray(TAG_FAVORITES);

                        for (int i = 0; i < favs.length(); i++) {
                            JSONObject c = favs.getJSONObject(i);
                            String title = c.getString(TAG_TITLE);
                            String imdb = c.getString(TAG_IMDB);

                            Movie m = new Movie (title,"","","","","", imdb);
                            favoriteMovies.add(m);
                        }
                    }

                    User logUser = new User(firstName, lastName, username, favoriteMovies);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Successful Login!", Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
                    String s = (new Gson().toJson(logUser)); //SERIALIZING OBJECT TO SEND IT TO NEXT ACTIVITY
                    intent.putExtra(USER, s);
                    startActivity(intent);

                } else {
                    // There is a user with that username
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();
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
