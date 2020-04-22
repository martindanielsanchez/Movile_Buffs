package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MoviePosterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);
    }

    /** Called when the user taps the Search Movies button */
    public void searchMovies(View view) {
        Intent intent = new Intent(this, MovieListActivity.class);
        startActivity(intent);
    }
}
