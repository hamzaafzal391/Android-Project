package com.cinefast.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SnacksFragment extends Fragment {

    public static final String ARG_MOVIE_NAME = "arg_movie_name";
    public static final String ARG_SEAT_COUNT = "arg_seat_count";
    public static final String ARG_TICKET_PRICE = "arg_ticket_price";
    public static final String ARG_SELECTED_SEATS = "arg_selected_seats";

    private String movieName = "Movie";
    private int seatCount = 1;
    private int ticketPrice = 16;
    private ArrayList<String> selectedSeats = new ArrayList<>();

    private TextView tvSnackTotal;
    private List<Snack> snacks;

    public static SnacksFragment newInstance(String movieName, int seatCount, int ticketPrice,
                                             ArrayList<String> selectedSeats) {
        SnacksFragment f = new SnacksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_NAME, movieName);
        args.putInt(ARG_SEAT_COUNT, seatCount);
        args.putInt(ARG_TICKET_PRICE, ticketPrice);
        args.putStringArrayList(ARG_SELECTED_SEATS, selectedSeats);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_snacks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String n = args.getString(ARG_MOVIE_NAME);
            if (n != null) movieName = n;
            seatCount = args.getInt(ARG_SEAT_COUNT, 1);
            ticketPrice = args.getInt(ARG_TICKET_PRICE, 16);
            ArrayList<String> seats = args.getStringArrayList(ARG_SELECTED_SEATS);
            if (seats != null) selectedSeats = seats;
        }

        tvSnackTotal = view.findViewById(R.id.tvSnackTotal);
        snacks = SnackRepository.getSnacks(requireContext());

        ListView lv = view.findViewById(R.id.lvSnacks);
        SnackAdapter adapter = new SnackAdapter(requireContext(), snacks, this::updateTotal);
        lv.setAdapter(adapter);

        view.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            double snacksTotal = getSnacksTotal();
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).openTicketSummary(
                        movieName,
                        seatCount,
                        ticketPrice,
                        snacksTotal,
                        selectedSeats,
                        buildSnackSummary()
                );
            }
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

