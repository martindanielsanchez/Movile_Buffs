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
    EditText password2;

    String first;
    String last;
    String user;
    String pass;
    String pass2;

    // url to create new product
    private static String url_create_user = "http://192.168.135.1:80/android_connect/create_user.php";
    private static String url_check_username = "http://192.168.135.1:80/android_connect/check_username.php";


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
        password2 = (EditText) findViewById(R.id.editTextPass2);


    }

    /** Called when the user taps the Save button */
    public void save(View view) {
        //SAVE INFO INTO VARIABLES
        first = firstName.getText().toString();
        last = lastName.getText().toString();
        user = username.getText().toString();
        pass = password.getText().toString();
        pass2 = password2.getText().toString();

        //CHECK ALL FIELDS ARE FILLED. IF NOT ALERT USER.
        if(!first.isEmpty() && !last.isEmpty() && !user.isEmpty() && !pass.isEmpty() && !pass2.isEmpty()) {
            //CHECK THAT PASSWORDS MATCH
            if(pass.equals(pass2)) {
                new CreateNewUser().execute(); // creating new user in background thread
            }
            else{
                Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
        }
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

            //First check if username is already taken by calling webservice that in turn calls a stored procedure
            // Building Parameters
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("username", user));
            // getting JSON Object
            // Note that check username url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(url_check_username,
                    "POST", params1);

            // check log cat fro response
            Log.d("Create Response", json1.toString());

            // check for success tag
            try {
                int success1 = json1.getInt(TAG_SUCCESS);

                if (success1 == 1) {
                    // There is no user with that username
                    //NOW WE WILL CREATE THE USER
                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", user));
                    params.add(new BasicNameValuePair("password", pass));
                    params.add(new BasicNameValuePair("first_name", first));
                    params.add(new BasicNameValuePair("last_name", last));

                    // getting JSON Object
                    // Note that create user url accepts POST method
                    JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                            "POST", params);

                    // check log cat fro response
                    Log.d("Create Response", json.toString());
                    // check for success tag
                    try {
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // successfully created user
                            runOnUiThread(new Runnable() {

                                public void run() {

                                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

                                }
                            });
                            // closing this screen
                            finish();
                        } else {
                            // failed to create product
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Could not create user, check your internet connection!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // There is a user with that username
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "There is Already a user with that Username!", Toast.LENGTH_LONG).show();
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
