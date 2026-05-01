package com.cinefast.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("CineFAST", "MainActivity launched");
        Toast.makeText(this, "MainActivity launched", Toast.LENGTH_SHORT).show();

        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nav = findViewById(R.id.navigationView);
        nav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                replaceRoot(new HomeFragment());
            } else if (id == R.id.nav_my_bookings) {
                getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                replaceRoot(new MyBookingsFragment());
            } else if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                SessionManagerV3.clear(this);
                Intent i = new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        if (savedInstanceState == null) {
            nav.setCheckedItem(R.id.nav_home);
            replaceRoot(new HomeFragment());
        }
    }

    private void replaceRoot(@NonNull androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentContainer, fragment)
                .commit();
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

