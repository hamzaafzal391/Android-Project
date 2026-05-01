package com.cinefast.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketSummaryFragment extends Fragment {

    public static final String ARG_MOVIE_NAME = "arg_movie_name";
    public static final String ARG_SEAT_COUNT = "arg_seat_count";
    public static final String ARG_TICKET_PRICE = "arg_ticket_price";
    public static final String ARG_SNACKS_TOTAL = "arg_snacks_total";
    public static final String ARG_SELECTED_SEATS = "arg_selected_seats";
    public static final String ARG_SNACK_ITEMS = "arg_snack_items";

    private static final int PRICE_PER_SEAT = 16;
    private static final String PREFS_NAME = "cinefast_prefs";
    private static final String KEY_LAST_MOVIE_NAME = "last_movie_name";
    private static final String KEY_LAST_SEAT_COUNT = "last_seat_count";
    private static final String KEY_LAST_TOTAL_CENTS = "last_total_cents";

    public static TicketSummaryFragment newInstance(String movieName, int seatCount, int ticketPrice,
                                                    double snacksTotal, ArrayList<String> selectedSeats,
                                                    String snackItems) {
        TicketSummaryFragment f = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_NAME, movieName);
        args.putInt(ARG_SEAT_COUNT, seatCount);
        args.putInt(ARG_TICKET_PRICE, ticketPrice);
        args.putDouble(ARG_SNACKS_TOTAL, snacksTotal);
        args.putStringArrayList(ARG_SELECTED_SEATS, selectedSeats);
        args.putString(ARG_SNACK_ITEMS, snackItems);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_ticket_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle b = getArguments();
        String movieName = b != null ? b.getString(ARG_MOVIE_NAME) : null;
        int seatCount = b != null ? b.getInt(ARG_SEAT_COUNT, 1) : 1;
        int ticketPrice = b != null ? b.getInt(ARG_TICKET_PRICE, PRICE_PER_SEAT) : PRICE_PER_SEAT;
        double snacksTotal = b != null ? b.getDouble(ARG_SNACKS_TOTAL, 0.0) : 0.0;
        ArrayList<String> selectedSeats = b != null ? b.getStringArrayList(ARG_SELECTED_SEATS) : null;
        String snackItems = b != null ? b.getString(ARG_SNACK_ITEMS) : null;

        if (movieName == null) movieName = "Movie";
        if (selectedSeats == null) selectedSeats = new ArrayList<>();
        if (snackItems == null || snackItems.isEmpty()) snackItems = "No snacks selected";

        double totalPrice = ticketPrice + snacksTotal;
        saveLastBooking(movieName, seatCount, totalPrice);
        saveBookingToFirebase(movieName, seatCount, totalPrice);

        TextView tvMovieName = view.findViewById(R.id.tvMovieName);
        TextView tvTicketsList = view.findViewById(R.id.tvTicketsList);
        TextView tvSnacksList = view.findViewById(R.id.tvSnacksList);
        TextView tvTotalPrice = view.findViewById(R.id.tvTotalPrice);

        tvMovieName.setText(movieName);
        tvTicketsList.setText(buildTicketsList(selectedSeats));
        tvSnacksList.setText(snackItems);
        tvTotalPrice.setText(String.format("%.2f USD", totalPrice));

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        String finalMovieName = movieName;
        ArrayList<String> finalSelectedSeats = selectedSeats;
        String finalSnackItems = snackItems;
        view.findViewById(R.id.btnSendTicket).setOnClickListener(v -> shareTicket(
                finalMovieName, seatCount, ticketPrice, snacksTotal, totalPrice, finalSelectedSeats, finalSnackItems));
    }

    private void saveLastBooking(String movieName, int seatCount, double totalPrice) {
        int cents = (int) Math.round(totalPrice * 100.0);
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, 0);
        prefs.edit()
                .putString(KEY_LAST_MOVIE_NAME, movieName)
                .putInt(KEY_LAST_SEAT_COUNT, seatCount)
                .putInt(KEY_LAST_TOTAL_CENTS, cents)
                .apply();
    }

    private void saveBookingToFirebase(String movieName, int seatCount, double totalPrice) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : SessionManagerV3.getUid(requireContext());
        if (uid == null) return;

        String bookingId = FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(uid)
                .push()
                .getKey();
        if (bookingId == null) return;

        // We don't have explicit showtime selection; store a future timestamp so cancellation rule is testable.
        long showTimeMillis = System.currentTimeMillis() + (24L * 60L * 60L * 1000L);

        String poster = MovieRepository.getPosterNameForMovie(requireContext(), movieName);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", uid);
        data.put("movieName", movieName);
        data.put("seatCount", seatCount);
        data.put("totalPrice", totalPrice);
        data.put("showTimeMillis", showTimeMillis);
        data.put("poster", poster);

        FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(uid)
                .child(bookingId)
                .setValue(data);
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

