package com.cinefast.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public final class SnackRepository {
    private SnackRepository() {}

    public static List<Snack> getSnacks(Context context) {
        ArrayList<Snack> out = new ArrayList<>();
        SnackDbHelper helper = new SnackDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        try (Cursor c = db.query(
                SnackDbHelper.TABLE_SNACKS,
                new String[]{SnackDbHelper.COL_NAME, SnackDbHelper.COL_PRICE, SnackDbHelper.COL_IMAGE},
                null,
                null,
                null,
                null,
                SnackDbHelper.COL_ID + " ASC"
        )) {
            while (c.moveToNext()) {
                String name = c.getString(0);
                double price = c.getDouble(1);
                String imageName = c.getString(2);
                int imageResId = context.getResources().getIdentifier(
                        imageName,
                        "drawable",
                        context.getPackageName()
                );
                if (imageResId == 0) imageResId = R.drawable.movie_poster_placeholder;
                out.add(new Snack(name, price, imageResId));
            }
        }

        return out;
    }
}

