package com.cinefast.app;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.VH> {

    private final List<Booking> items;

    public BookingsAdapter(List<Booking> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Booking b = items.get(position);
        h.tvMovie.setText(b.movieName);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy · hh:mm a", Locale.getDefault());
        h.tvDateTime.setText(sdf.format(new Date(b.showTimeMillis)));
        h.tvTickets.setText("Tickets: " + b.seatCount);

        int posterResId = 0;
        if (b.poster != null && !b.poster.isEmpty()) {
            posterResId = h.itemView.getContext().getResources().getIdentifier(
                    b.poster,
                    "drawable",
                    h.itemView.getContext().getPackageName()
            );
        }
        if (posterResId == 0) posterResId = R.drawable.movie_poster_placeholder;
        h.ivPoster.setImageResource(posterResId);

        h.btnCancel.setOnClickListener(v -> confirmCancel(v.getContext(), b));
    }

    private void confirmCancel(Context context, Booking booking) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (d, w) -> tryCancel(context, booking))
                .setNegativeButton("No", (d, w) -> d.dismiss())
                .show();
    }

    private void tryCancel(Context context, Booking booking) {
        if (booking.showTimeMillis <= System.currentTimeMillis()) {
            Toast.makeText(context, "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            return;
        }
        if (booking.userId == null || booking.bookingId == null) {
            Toast.makeText(context, "Cancellation failed", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(booking.userId)
                .child(booking.bookingId)
                .removeValue()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Toast.makeText(context, "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Cancellation failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView ivPoster;
        final TextView tvMovie;
        final TextView tvDateTime;
        final TextView tvTickets;
        final Button btnCancel;

        VH(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvMovie = itemView.findViewById(R.id.tvMovie);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTickets = itemView.findViewById(R.id.tvTickets);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}

