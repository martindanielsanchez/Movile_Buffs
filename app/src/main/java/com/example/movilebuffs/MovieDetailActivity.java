package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
    }

    /**
     * Called when the user taps the View Poster button
     * Navigates to MoviePosterActivity with the selected Movie
     * */
    public void viewPoster(View view) {
        Intent intent = new Intent(this, MoviePosterActivity.class);
        startActivity(intent);
    }
}
