package com.example.movilebuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MoviePosterActivity extends AppCompatActivity {
    private String userJSON = null;
    private String movieJSON = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);
        Intent intent = getIntent();

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String posterLink = intent.getStringExtra(MovieDetailActivity.POSTER); //get poster link
        userJSON = intent.getStringExtra(MainActivity.USER); //get user json string
        movieJSON = intent.getStringExtra(MovieDetailActivity.MOVIE); //get movie json string

        Picasso.get().load(posterLink).into(imageView);
    }

    /**
     * Called when the user taps the Search Movies button
     * Navigates to MovieListActivity
     * */
    public void searchMovies(View view) {
        Intent intent = new Intent(this, MovieListActivity.class);
        intent.putExtra(MainActivity.USER, userJSON); //also send USER
        startActivity(intent);
    }
}
