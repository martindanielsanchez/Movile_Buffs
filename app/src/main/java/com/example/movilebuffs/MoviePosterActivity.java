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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);
        Intent intent = getIntent();

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String posterLink = intent.getStringExtra(MainActivity.EXTRA_MESSAGE); //get poster link

        /*
        URL url = null;
        try {
            url = new URL(posterLink);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        Picasso.get().load(posterLink).into(imageView);
        Toast.makeText(MoviePosterActivity.this, "Link: " + posterLink, Toast.LENGTH_LONG).show();
    }

    /**
     * Called when the user taps the Search Movies button
     * Navigates to MovieListActivity
     * */
    public void searchMovies(View view) {
        Intent intent = new Intent(this, MovieListActivity.class);
        //need to send user too
        startActivity(intent);
    }
}
