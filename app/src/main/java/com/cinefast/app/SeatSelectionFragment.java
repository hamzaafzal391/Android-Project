package com.cinefast.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatSelectionFragment extends Fragment {

    public static final String ARG_MOVIE_NAME = "arg_movie_name";
    public static final String ARG_MOVIE_TYPE = "arg_movie_type";

    private static final int PRICE_PER_SEAT = 16;
    private static final String[] ROWS = {"A", "B", "C", "D", "E"};
    private static final int SEATS_PER_ROW = 8;
    private static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=";

    private String movieName = "Movie";
    private boolean isComingSoonMovie = false;

    private final Set<String> selectedSeats = new HashSet<>();
    private final Set<String> bookedSeats = new HashSet<>();
    private final List<View> seatViews = new ArrayList<>();
    private boolean seatsReserved = false;

    private TextView tvSelectedSeats;
    private Button btnProceedToSnacks;
    private Button btnBookSeats;

    public static SeatSelectionFragment newInstance(String movieName, String movieType) {
        SeatSelectionFragment f = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_NAME, movieName);
        args.putString(ARG_MOVIE_TYPE, movieType);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_seat_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString(ARG_MOVIE_NAME);
            if (name != null) movieName = name;
            String type = args.getString(ARG_MOVIE_TYPE);
            isComingSoonMovie = MoviesAdapter.MOVIE_TYPE_COMING_SOON.equals(type);
        }

        tvSelectedSeats = view.findViewById(R.id.tvSelectedSeats);
        btnProceedToSnacks = view.findViewById(R.id.btnProceedToSnacks);
        btnBookSeats = view.findViewById(R.id.btnBookSeats);
        btnBookSeats.setEnabled(false);

        TextView tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
        tvMovieTitle.setText(movieName);

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        initBookedSeats();
        buildSeatGrid(view);

        if (isComingSoonMovie) {
            applyComingSoonMode();
        } else {
            btnBookSeats.setOnClickListener(v -> confirmBooking());
            btnProceedToSnacks.setOnClickListener(v -> {
                seatsReserved = true;
                if (requireActivity() instanceof MainActivity) {
                    ((MainActivity) requireActivity()).openSnacks(
                            movieName,
                            selectedSeats.size(),
                            selectedSeats.size() * PRICE_PER_SEAT,
                            new ArrayList<>(selectedSeats)
                    );
                }
            });
        }
    }

    private void initBookedSeats() {
        bookedSeats.add("A1");
        bookedSeats.add("A2");
        bookedSeats.add("B3");
        bookedSeats.add("C5");
        bookedSeats.add("D2");
        bookedSeats.add("E7");
        bookedSeats.add("E8");
    }

    private void buildSeatGrid(View root) {
        LinearLayout seatGrid = root.findViewById(R.id.seatGrid);
        int sizeDp = (int) (36 * getResources().getDisplayMetrics().density);
        int marginDp = (int) (4 * getResources().getDisplayMetrics().density);

        for (String row : ROWS) {
            LinearLayout rowLayout = new LinearLayout(requireContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int s = 1; s <= SEATS_PER_ROW; s++) {
                String seatId = row + s;
                View seat = new View(requireContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeDp, sizeDp);
                params.setMargins(marginDp, marginDp, marginDp, marginDp);
                seat.setLayoutParams(params);
                seat.setTag(seatId);
                seat.setClickable(true);
                seat.setFocusable(true);

                if (bookedSeats.contains(seatId)) {
                    seat.setBackgroundResource(R.drawable.seat_booked);
                    seat.setEnabled(false);
                } else {
                    seat.setBackgroundResource(R.drawable.seat_available);
                    if (!isComingSoonMovie) {
                        seat.setOnClickListener(v -> toggleSeat((View) v));
                    } else {
                        seat.setEnabled(false);
                    }
                }
                seatViews.add(seat);
                rowLayout.addView(seat);
            }
            seatGrid.addView(rowLayout);
        }
    }

    private void toggleSeat(View seat) {
        if (seatsReserved) return;
        String seatId = (String) seat.getTag();
        if (bookedSeats.contains(seatId)) return;

        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            seat.setBackgroundResource(R.drawable.seat_available);
        } else {
            selectedSeats.add(seatId);
            seat.setBackgroundResource(R.drawable.seat_selected);
        }
        updateUI();
    }

    private void updateUI() {
        int total = selectedSeats.size() * PRICE_PER_SEAT;
        tvSelectedSeats.setText("Selected: " + selectedSeats.size() + " seats · $" + total);
        boolean hasSeats = selectedSeats.size() > 0;
        btnProceedToSnacks.setEnabled(hasSeats);
        btnBookSeats.setEnabled(hasSeats);
    }

    private void applyComingSoonMode() {
        tvSelectedSeats.setText("Coming Soon: seat selection is disabled");

        btnBookSeats.setText("Coming Soon");
        btnBookSeats.setEnabled(false);

        btnProceedToSnacks.setEnabled(true);
        btnProceedToSnacks.setText("Watch Trailer");
        btnProceedToSnacks.setOnClickListener(v -> openTrailer());
    }

    private void openTrailer() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(YOUTUBE_SEARCH_URL + Uri.encode(movieName + " trailer")));
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void confirmBooking() {
        Toast.makeText(requireContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).openTicketSummary(
                    movieName,
                    selectedSeats.size(),
                    selectedSeats.size() * PRICE_PER_SEAT,
                    0.0,
                    new ArrayList<>(selectedSeats),
                    ""
            );
        }
    }
}

