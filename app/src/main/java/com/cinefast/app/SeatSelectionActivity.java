package com.cinefast.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_NAME = "movie_name";
    public static final String EXTRA_SELECTED_SEATS = "selected_seats";
    public static final String EXTRA_TOTAL_PRICE = "total_price";
    public static final String EXTRA_SKIP_SNACKS = "skip_snacks";

    private static final int PRICE_PER_SEAT = 16;
    private static final String[] ROWS = {"A", "B", "C", "D", "E"};
    private static final int SEATS_PER_ROW = 8;

    private String movieName;
    private Set<String> selectedSeats = new HashSet<>();
    private Set<String> bookedSeats = new HashSet<>();
    private List<View> seatViews = new ArrayList<>();
    private boolean seatsReserved = false;

    private TextView tvSelectedSeats;
    private Button btnProceedToSnacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        movieName = getIntent().getStringExtra(EXTRA_MOVIE_NAME);
        if (movieName == null) movieName = "Movie";

        tvSelectedSeats = findViewById(R.id.tvSelectedSeats);
        btnProceedToSnacks = findViewById(R.id.btnProceedToSnacks);
        Button btnBookSeats = findViewById(R.id.btnBookSeats);
        btnBookSeats.setEnabled(false);

        TextView tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieTitle.setText(movieName);

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());

        initBookedSeats();
        buildSeatGrid();

        btnBookSeats.setOnClickListener(v -> confirmBooking(true));
        btnProceedToSnacks.setOnClickListener(v -> {
            seatsReserved = true;
            Intent intent = new Intent(SeatSelectionActivity.this, SnacksActivity.class);
            intent.putExtra(SnacksActivity.EXTRA_MOVIE_NAME, movieName);
            intent.putExtra(SnacksActivity.EXTRA_SEAT_COUNT, selectedSeats.size());
            intent.putExtra(SnacksActivity.EXTRA_TICKET_PRICE, selectedSeats.size() * PRICE_PER_SEAT);
            intent.putStringArrayListExtra(SnacksActivity.EXTRA_SELECTED_SEATS, new ArrayList<>(selectedSeats));
            startActivity(intent);
            finish();
        });
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

    private void buildSeatGrid() {
        LinearLayout seatGrid = findViewById(R.id.seatGrid);
        int sizeDp = (int) (36 * getResources().getDisplayMetrics().density);
        int marginDp = (int) (4 * getResources().getDisplayMetrics().density);

        for (String row : ROWS) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int s = 1; s <= SEATS_PER_ROW; s++) {
                String seatId = row + s;
                View seat = new View(this);
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
                    seat.setOnClickListener(v -> toggleSeat((View) v));
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
        findViewById(R.id.btnBookSeats).setEnabled(hasSeats);
    }

    private void confirmBooking(boolean skipSnacks) {
        int ticketPrice = selectedSeats.size() * PRICE_PER_SEAT;
        Intent intent = new Intent(SeatSelectionActivity.this, TicketSummaryActivity.class);
        intent.putExtra(TicketSummaryActivity.EXTRA_MOVIE_NAME, movieName);
        intent.putExtra(TicketSummaryActivity.EXTRA_SEAT_COUNT, selectedSeats.size());
        intent.putExtra(TicketSummaryActivity.EXTRA_TICKET_PRICE, ticketPrice);
        intent.putExtra(TicketSummaryActivity.EXTRA_SNACKS_TOTAL, 0);
        intent.putStringArrayListExtra(TicketSummaryActivity.EXTRA_SELECTED_SEATS, new ArrayList<>(selectedSeats));
        startActivity(intent);
        finish();
    }
}
