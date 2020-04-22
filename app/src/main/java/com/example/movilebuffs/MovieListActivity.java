package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

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
        textView.setText(logged.getFirstName());
    }
}
