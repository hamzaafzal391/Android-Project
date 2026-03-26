package com.cinefast.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private static final String PREFS_NAME = "cinefast_prefs";
    private static final String KEY_LAST_MOVIE_NAME = "last_movie_name";
    private static final String KEY_LAST_SEAT_COUNT = "last_seat_count";
    private static final String KEY_LAST_TOTAL_CENTS = "last_total_cents";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton menuBtn = view.findViewById(R.id.btnHomeMenu);
        menuBtn.setOnClickListener(v -> showHomeMenu(v));

        TabLayout tabs = view.findViewById(R.id.homeTabs);
        ViewPager2 pager = view.findViewById(R.id.homePager);
        pager.setAdapter(new HomePagerAdapter(requireActivity()));

        new TabLayoutMediator(tabs, pager, (tab, position) -> {
            if (position == 0) tab.setText("Now Showing");
            else tab.setText("Coming Soon");
        }).attach();
    }

    private void showHomeMenu(View anchor) {
        PopupMenu popup = new PopupMenu(requireContext(), anchor);
        popup.getMenuInflater().inflate(R.menu.home_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this::onHomeMenuItemSelected);
        popup.show();
    }

    private boolean onHomeMenuItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_last_booking) {
            showLastBookingDialog();
            return true;
        }
        return false;
    }

    private void showLastBookingDialog() {
        String movie = requireContext()
                .getSharedPreferences(PREFS_NAME, 0)
                .getString(KEY_LAST_MOVIE_NAME, null);

        int seats = requireContext()
                .getSharedPreferences(PREFS_NAME, 0)
                .getInt(KEY_LAST_SEAT_COUNT, 0);

        int cents = requireContext()
                .getSharedPreferences(PREFS_NAME, 0)
                .getInt(KEY_LAST_TOTAL_CENTS, -1);

        String message;
        if (movie == null || cents < 0) {
            message = "No previous booking found.";
        } else {
            message = "Last Booking\n\n"
                    + "Movie: " + movie + "\n"
                    + "Seats: " + seats + "\n"
                    + "Total Price: $" + String.format("%.2f", cents / 100.0);
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("View Last Booking")
                .setMessage(message)
                .setPositiveButton("OK", (d, w) -> d.dismiss())
                .show();
    }
}

