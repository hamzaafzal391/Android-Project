package com.cinefast.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupMovie(view, R.id.btnBookSeats1, R.id.btnTrailer1, "The Dark Knight");
        setupMovie(view, R.id.btnBookSeats2, R.id.btnTrailer2, "Inception");
        setupMovie(view, R.id.btnBookSeats3, R.id.btnTrailer3, "Interstellar");
        setupMovie(view, R.id.btnBookSeats4, R.id.btnTrailer4, "The Shawshank Redemption");
    }

    private void setupMovie(View root, int bookBtnId, int trailerBtnId, String movieName) {
        root.findViewById(bookBtnId).setOnClickListener(v -> openSeatSelection(movieName));
        root.findViewById(trailerBtnId).setOnClickListener(v -> openTrailer(movieName));
    }

    private void openTrailer(String movieName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(YOUTUBE_SEARCH_URL + movieName + "+trailer"));
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openSeatSelection(String movieName) {
        // Temporary: keep Assignment-1 flow working. Later commits will navigate to SeatSelectionFragment.
        Intent intent = new Intent(requireContext(), SeatSelectionActivity.class);
        intent.putExtra(SeatSelectionActivity.EXTRA_MOVIE_NAME, movieName);
        startActivity(intent);
    }
}

