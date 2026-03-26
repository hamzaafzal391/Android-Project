package com.cinefast.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TicketSummaryActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_NAME = "movie_name";
    public static final String EXTRA_SEAT_COUNT = "seat_count";
    public static final String EXTRA_TICKET_PRICE = "ticket_price";
    public static final String EXTRA_SNACKS_TOTAL = "snacks_total";
    public static final String EXTRA_SELECTED_SEATS = "selected_seats";
    public static final String EXTRA_SNACK_ITEMS = "snack_items";

    private static final int PRICE_PER_SEAT = 16;
    private static final String PREFS_NAME = "cinefast_prefs";
    private static final String KEY_LAST_MOVIE_NAME = "last_movie_name";
    private static final String KEY_LAST_SEAT_COUNT = "last_seat_count";
    private static final String KEY_LAST_TOTAL_CENTS = "last_total_cents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_summary);

        String movieName = getIntent().getStringExtra(EXTRA_MOVIE_NAME);
        int seatCount = getIntent().getIntExtra(EXTRA_SEAT_COUNT, 1);
        int ticketPrice = getIntent().getIntExtra(EXTRA_TICKET_PRICE, PRICE_PER_SEAT);
        double snacksTotal = getIntent().getDoubleExtra(EXTRA_SNACKS_TOTAL, 0);
        ArrayList<String> selectedSeats = getIntent().getStringArrayListExtra(EXTRA_SELECTED_SEATS);
        String snackItems = getIntent().getStringExtra(EXTRA_SNACK_ITEMS);

        if (movieName == null) movieName = "Movie";
        if (selectedSeats == null) selectedSeats = new ArrayList<>();
        if (snackItems == null || snackItems.isEmpty()) snackItems = "No snacks selected";

        double totalPrice = ticketPrice + snacksTotal;
        saveLastBooking(movieName, seatCount, totalPrice);

        TextView tvMovieName = findViewById(R.id.tvMovieName);
        TextView tvTicketsList = findViewById(R.id.tvTicketsList);
        TextView tvSnacksList = findViewById(R.id.tvSnacksList);
        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);

        tvMovieName.setText(movieName);
        tvTicketsList.setText(buildTicketsList(selectedSeats));
        tvSnacksList.setText(snackItems);
        tvTotalPrice.setText(String.format("%.2f USD", totalPrice));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnSendTicket).setOnClickListener(v -> shareTicket(
                movieName, seatCount, ticketPrice, snacksTotal, totalPrice, selectedSeats, snackItems));
    }

    private void saveLastBooking(String movieName, int seatCount, double totalPrice) {
        int cents = (int) Math.round(totalPrice * 100.0);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_LAST_MOVIE_NAME, movieName)
                .putInt(KEY_LAST_SEAT_COUNT, seatCount)
                .putInt(KEY_LAST_TOTAL_CENTS, cents)
                .apply();
    }

    private String buildTicketsList(ArrayList<String> seats) {
        if (seats.isEmpty()) {
            return "No seats selected";
        }
        StringBuilder sb = new StringBuilder();
        for (String seat : seats) {
            if (sb.length() > 0) sb.append("\n");
            String row = seat.substring(0, 1);
            String num = seat.substring(1);
            sb.append("Row ").append(row).append(", Seat ").append(num).append(" - ").append(PRICE_PER_SEAT).append(" USD");
        }
        return sb.toString();
    }

    private void shareTicket(String movieName, int seatCount, int ticketPrice,
                            double snacksTotal, double totalPrice,
                            ArrayList<String> seats, String snackItems) {
        String ticketText = buildTicketText(movieName, seatCount, ticketPrice, snacksTotal, totalPrice, seats, snackItems);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CineFAST - Your Movie Ticket");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ticketText);
        startActivity(Intent.createChooser(shareIntent, "Share ticket via"));
    }

    private String buildTicketText(String movieName, int seatCount, int ticketPrice,
                                   double snacksTotal, double totalPrice,
                                   ArrayList<String> seats, String snackItems) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CineFAST Ticket ===\n\n");
        sb.append("Movie: ").append(movieName).append("\n");
        sb.append("Seats: ").append(seatCount).append("\n");
        if (!seats.isEmpty()) {
            sb.append("Seat Details:\n");
            for (String seat : seats) {
                sb.append("  - Row ").append(seat.charAt(0)).append(", Seat ").append(seat.substring(1)).append("\n");
            }
        }
        sb.append("Ticket Total: $").append(ticketPrice).append("\n");
        sb.append("Snacks: ").append(snackItems).append("\n");
        sb.append("Snacks Total: $").append(String.format("%.2f", snacksTotal)).append("\n");
        sb.append("TOTAL: $").append(String.format("%.2f", totalPrice)).append(" USD\n\n");
        sb.append("Theater Stars (90°Mall) · Hall 1st\n");
        sb.append("Date: 13.04.2025 · Time: 22:15");
        return sb.toString();
    }
}
