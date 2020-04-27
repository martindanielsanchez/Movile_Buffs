package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    public static final String USER = "com.example.movilebuffs.USER";

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
        Intent intent = new Intent(this, MovieListActivity.class);
        EditText ed1 = (EditText) findViewById(R.id.editTextUser);
        EditText ed2 = (EditText) findViewById(R.id.editTextPass);

        String username = ed1.getText().toString();
        String pass = ed2.getText().toString();

        //check if username is in database and retrieve it, if not alert user

        User logUser = new User("Valentino", "Rossi", "vale46", "123", new ArrayList<Movie>()); //CREATING USER FOR TESTING

        Boolean validate = logUser.validateLogIn(pass);
        if(validate){
            String s = (new Gson().toJson(logUser)); //SERIALIZING OBJECT TO SEND IT TO NEXT ACTIVITY
            intent.putExtra(USER, s);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when the user taps the Sign Up button
     * Navigates to SignUpActivity
     * */
    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


}
