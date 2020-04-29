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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;

    String first;
    String last;
    String user;
    String pass;

    // url to create new product
    //private static String url_create_user = "https://api.androidhive.info/android_connect/create_user.php";
  //  private static String url_create_user = "https://localhost/android_connect/create_user.php";
    private static String url_create_user = "http://192.168.135.1:80/android_connect/create_user.php";
    //C:\xampp\htdocs\android_connect\create_user.php
   // private static String url_create_user = "C:\\xampp\\htdocs\\android_connect\\create_user.php";
    //http://localhost/android_connect/test.php

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = (EditText) findViewById(R.id.editTextFirst);
        lastName = (EditText) findViewById(R.id.editTextLast);
        username = (EditText) findViewById(R.id.editTextUser);
        password = (EditText) findViewById(R.id.editTextPass);


    }

    /** Called when the user taps the Save button */
    public void save(View view) {

        //CHECK ALL FIELDS ARE FILLED. IF NOT ALERT USER.

        //SAVE INFO INTO VARIABLES

        first = firstName.getText().toString();
        last = lastName.getText().toString();
        user = username.getText().toString();
        pass = password.getText().toString();
/*
first = "Andrea";
last = "Dovizioso";
user = "dovi04";
pass = "andrea04";
*/
            //CHECK THAT PASSWORDS MATCH. IF NOT ALERT USER.
        //CHECK WITH DATABASE IF USERNAME EXISTS. IF NOT UPDATE DATABASE.

        new CreateNewUser().execute(); // creating new user in background thread

        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }

    /** Called when the user taps the Back button */
    public void back(View view) {
        //Simply goes back to main page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            /*
            String first = firstName.getText().toString();
            String last = lastName.getText().toString();
            String user = username.getText().toString();
            String pass = password.getText().toString();*/

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("password", pass));
            params.add(new BasicNameValuePair("first_name", first));
            params.add(new BasicNameValuePair("last_name", last));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    //Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                    //startActivity(i);
                    runOnUiThread(new Runnable() {

                        public void run() {

                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

                        }
                    });
                   // Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

                    // closing this screen
                   // finish();
                } else {
                    // failed to create product
                    runOnUiThread(new Runnable() {

                        public void run() {

                            Toast.makeText(getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();

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
