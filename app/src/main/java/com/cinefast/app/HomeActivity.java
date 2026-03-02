package com.cinefast.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupMovie1();
        setupMovie2();
        setupMovie3();
        setupMovie4();
    }

    private void openTrailer(String movieName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(YOUTUBE_SEARCH_URL + movieName + "+trailer"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openSeatSelection(String movieName) {
        Intent intent = new Intent(HomeActivity.this, SeatSelectionActivity.class);
        intent.putExtra(SeatSelectionActivity.EXTRA_MOVIE_NAME, movieName);
        startActivity(intent);
    }

    private void setupMovie1() {
        String movieName = "The Dark Knight";
        findViewById(R.id.btnBookSeats1).setOnClickListener(v -> openSeatSelection(movieName));
        findViewById(R.id.btnTrailer1).setOnClickListener(v -> openTrailer(movieName));
    }

    private void setupMovie2() {
        String movieName = "Inception";
        findViewById(R.id.btnBookSeats2).setOnClickListener(v -> openSeatSelection(movieName));
        findViewById(R.id.btnTrailer2).setOnClickListener(v -> openTrailer(movieName));
    }

    private void setupMovie3() {
        String movieName = "Interstellar";
        findViewById(R.id.btnBookSeats3).setOnClickListener(v -> openSeatSelection(movieName));
        findViewById(R.id.btnTrailer3).setOnClickListener(v -> openTrailer(movieName));
    }

    private void setupMovie4() {
        String movieName = "The Shawshank Redemption";
        findViewById(R.id.btnBookSeats4).setOnClickListener(v -> openSeatSelection(movieName));
        findViewById(R.id.btnTrailer4).setOnClickListener(v -> openTrailer(movieName));
    }
}
