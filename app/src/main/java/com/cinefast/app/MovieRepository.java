package com.cinefast.app;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieRepository {
    private MovieRepository() {}

    public static List<Movie> loadMovies(Context context) {
        ArrayList<Movie> list = new ArrayList<>();
        try {
            String json = AssetUtils.readAssetFile(context, "movies.json");
            JSONObject root = new JSONObject(json);
            JSONArray movies = root.optJSONArray("movies");
            if (movies == null) return list;

            for (int i = 0; i < movies.length(); i++) {
                JSONObject m = movies.optJSONObject(i);
                if (m == null) continue;

                String name = m.optString("name", "");
                String subtitle = m.optString("subtitle", "");
                String posterName = m.optString("poster", "");
                String trailerQuery = m.optString("trailerQuery", name + " trailer");
                String type = m.optString("type", MoviesAdapter.MOVIE_TYPE_NOW_SHOWING);

                boolean comingSoon = MoviesAdapter.MOVIE_TYPE_COMING_SOON.equals(type);
                int posterResId = context.getResources().getIdentifier(
                        posterName,
                        "drawable",
                        context.getPackageName()
                );
                if (posterResId == 0) posterResId = R.drawable.movie_poster_placeholder;

                if (!name.isEmpty()) {
                    list.add(new Movie(name, subtitle, posterResId, trailerQuery, comingSoon));
                }
            }
        } catch (Exception ignored) {
            // If JSON is missing or malformed, we return an empty list (UI won't crash).
        }
        return list;
    }

    public static List<Movie> nowShowing(Context context) {
        List<Movie> all = loadMovies(context);
        ArrayList<Movie> out = new ArrayList<>();
        for (Movie m : all) if (!m.isComingSoon()) out.add(m);
        return out;
    }

    public static List<Movie> comingSoon(Context context) {
        List<Movie> all = loadMovies(context);
        ArrayList<Movie> out = new ArrayList<>();
        for (Movie m : all) if (m.isComingSoon()) out.add(m);
        return out;
    }
}

