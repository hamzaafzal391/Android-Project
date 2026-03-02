package com.cinefast.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SnacksActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_NAME = "movie_name";
    public static final String EXTRA_SEAT_COUNT = "seat_count";
    public static final String EXTRA_TICKET_PRICE = "ticket_price";
    public static final String EXTRA_SELECTED_SEATS = "selected_seats";

    private static final double[] PRICES = {8.99, 7.99, 4.99, 5.99};

    private int[] quantities = {0, 0, 0, 0};
    private int seatCount;
    private int ticketPrice;
    private String movieName;
    private ArrayList<String> selectedSeats;

    private TextView tvSnackTotal;
    private TextView[] tvQtyViews;

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
        tvQtyViews = new TextView[]{
                findViewById(R.id.tvQty1),
                findViewById(R.id.tvQty2),
                findViewById(R.id.tvQty3),
                findViewById(R.id.tvQty4)
        };

        setupSnack(0, R.id.btnMinus1, R.id.btnPlus1);
        setupSnack(1, R.id.btnMinus2, R.id.btnPlus2);
        setupSnack(2, R.id.btnMinus3, R.id.btnPlus3);
        setupSnack(3, R.id.btnMinus4, R.id.btnPlus4);

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
    }

    private void setupSnack(int index, int minusId, int plusId) {
        findViewById(minusId).setOnClickListener(v -> {
            if (quantities[index] > 0) {
                quantities[index]--;
                tvQtyViews[index].setText(String.valueOf(quantities[index]));
                updateTotal();
            }
        });
        findViewById(plusId).setOnClickListener(v -> {
            quantities[index]++;
            tvQtyViews[index].setText(String.valueOf(quantities[index]));
            updateTotal();
        });
    }

    private void updateTotal() {
        double total = getSnacksTotal();
        tvSnackTotal.setText(String.format("Snacks Total: $%.2f", total));
    }

    private double getSnacksTotal() {
        double total = 0;
        for (int i = 0; i < 4; i++) {
            total += quantities[i] * PRICES[i];
        }
        return total;
    }

    private String buildSnackSummary() {
        StringBuilder sb = new StringBuilder();
        String[] names = {"Popcorn", "Nachos", "Soft Drink", "Candy Mix"};
        for (int i = 0; i < 4; i++) {
            if (quantities[i] > 0) {
                if (sb.length() > 0) sb.append("\n");
                sb.append("x").append(quantities[i]).append(" ").append(names[i])
                        .append(" - $").append(String.format("%.2f", quantities[i] * PRICES[i]));
            }
        }
        return sb.toString();
    }
}
