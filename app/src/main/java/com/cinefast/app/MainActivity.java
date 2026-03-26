package com.cinefast.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragmentContainer, new HomeFragment())
                    .commit();
        }
    }

    public void openSeatSelection(String movieName, String movieType) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentContainer, SeatSelectionFragment.newInstance(movieName, movieType))
                .addToBackStack("seat_selection")
                .commit();
    }

    public void openSnacks(String movieName, int seatCount, int ticketPrice, ArrayList<String> selectedSeats) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentContainer,
                        SnacksFragment.newInstance(movieName, seatCount, ticketPrice, selectedSeats))
                .addToBackStack("snacks")
                .commit();
    }

    public void openTicketSummary(String movieName, int seatCount, int ticketPrice,
                                  double snacksTotal, ArrayList<String> selectedSeats, String snackItems) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentContainer,
                        TicketSummaryFragment.newInstance(movieName, seatCount, ticketPrice, snacksTotal, selectedSeats, snackItems))
                .addToBackStack("ticket_summary")
                .commit();
    }
}

