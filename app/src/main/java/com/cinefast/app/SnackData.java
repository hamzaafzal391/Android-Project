package com.cinefast.app;

import java.util.ArrayList;
import java.util.List;

public final class SnackData {
    private SnackData() {}

    public static List<Snack> snacks() {
        ArrayList<Snack> list = new ArrayList<>();
        list.add(new Snack("Popcorn", 8.99, R.drawable.snack_popcorn));
        list.add(new Snack("Nachos", 7.99, R.drawable.snack_nachos));
        list.add(new Snack("Soft Drink", 4.99, R.drawable.snack_drink));
        list.add(new Snack("Candy Mix", 5.99, R.drawable.snack_candy));
        return list;
    }
}

