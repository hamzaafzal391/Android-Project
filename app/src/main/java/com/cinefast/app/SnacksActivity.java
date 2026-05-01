package com.cinefast.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SnacksActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_NAME = "movie_name";
    public static final String EXTRA_SEAT_COUNT = "seat_count";
    public static final String EXTRA_TICKET_PRICE = "ticket_price";
    public static final String EXTRA_SELECTED_SEATS = "selected_seats";

    private int seatCount;
    private int ticketPrice;
    private String movieName;
    private ArrayList<String> selectedSeats;

    private TextView tvSnackTotal;
    private List<Snack> snacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        movieName = getIntent().getStringExtra(EXTRA_MOVIE_NAME);
        seatCount = getIntent().getIntExtra(EXTRA_SEAT_COUNT, 1);
        ticketPrice = getIntent().getIntExtra(EXTRA_TICKET_PRICE, 16);
        selectedSeats = getIntent().getStringArrayListExtra(EXTRA_SELECTED_SEATS);
        if (selectedSeats == null) selectedSeats = new ArrayList<>();

        tvSnackTotal = findViewById(R.id.tvSnackTotal);
        snacks = SnackRepository.getSnacks(this);

        ListView lv = findViewById(R.id.lvSnacks);
        SnackAdapter adapter = new SnackAdapter(this, snacks, this::updateTotal);
        lv.setAdapter(adapter);

        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            double snacksTotal = getSnacksTotal();
            Intent intent = new Intent(SnacksActivity.this, TicketSummaryActivity.class);
            intent.putExtra(TicketSummaryActivity.EXTRA_MOVIE_NAME, movieName);
            intent.putExtra(TicketSummaryActivity.EXTRA_SEAT_COUNT, seatCount);
            intent.putExtra(TicketSummaryActivity.EXTRA_TICKET_PRICE, ticketPrice);
            intent.putExtra(TicketSummaryActivity.EXTRA_SNACKS_TOTAL, snacksTotal);
            intent.putStringArrayListExtra(TicketSummaryActivity.EXTRA_SELECTED_SEATS, selectedSeats);
            intent.putExtra(TicketSummaryActivity.EXTRA_SNACK_ITEMS, buildSnackSummary());
            startActivity(intent);
            finish();
        });

        updateTotal();
    }

    private void updateTotal() {
        double total = getSnacksTotal();
        tvSnackTotal.setText(String.format("Snacks Total: $%.2f", total));
    }

    private double getSnacksTotal() {
        double total = 0.0;
        for (Snack s : snacks) {
            total += s.getQuantity() * s.getPrice();
        }
        return total;
    }

    private String buildSnackSummary() {
        StringBuilder sb = new StringBuilder();
        for (Snack s : snacks) {
            if (s.getQuantity() > 0) {
                if (sb.length() > 0) sb.append("\n");
                sb.append("x").append(s.getQuantity()).append(" ").append(s.getName())
                        .append(" - $").append(String.format("%.2f", s.getQuantity() * s.getPrice()));
            }
        }
        return sb.toString();
    }
}
