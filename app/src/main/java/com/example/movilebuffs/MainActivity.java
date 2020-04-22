package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.movilebuffs.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Log In button */
    public void logIn(View view) {
        Intent intent = new Intent(this, MovieListActivity.class);
        /*
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();*/
        User logUser = new User("Valentino", "Rossi", "vale46", "123"); //CREATING USER FOR TESTING
        String s = (new Gson().toJson(logUser)); //SERIALIZING OBJECT TO SEND IT TO NEXT ACTIVITY
        intent.putExtra(EXTRA_MESSAGE, s);
        startActivity(intent);
    }

    /** Called when the user taps the Log In button */
    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


}
