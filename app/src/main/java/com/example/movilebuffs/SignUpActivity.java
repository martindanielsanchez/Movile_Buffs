package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /** Called when the user taps the Save button */
    public void save(View view) {

        //SAVE INFO INTO VARIABLES
            //CHECK ALL FIELDS ARE FILLED. IF NOT ALERT USER.
            //CHECK THAT PASSWORDS MATCH. IF NOT ALERT USER.
        //CHECK WITH DATABASE IF USERNAME EXISTS. IF NOT UPDATE DATABASE.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the Back button */
    public void back(View view) {
        //Simply goes back to main page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
