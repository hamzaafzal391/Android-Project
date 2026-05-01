package com.cinefast.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyBookingsFragment extends Fragment {

    private final ArrayList<Booking> bookings = new ArrayList<>();
    private BookingsAdapter adapter;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmpty = view.findViewById(R.id.tvEmpty);
        RecyclerView rv = view.findViewById(R.id.rvBookings);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BookingsAdapter(bookings);
        rv.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : SessionManagerV3.getUid(requireContext());
        if (uid == null) {
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookings.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Booking b = child.getValue(Booking.class);
                            if (b == null) continue;
                            b.bookingId = child.getKey();
                            b.userId = uid;
                            bookings.add(b);
                        }
                        Collections.sort(bookings, Comparator.comparingLong(o -> o.showTimeMillis));
                        adapter.notifyDataSetChanged();
                        tvEmpty.setVisibility(bookings.isEmpty() ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }
}

