package com.cinefast.app;

import java.util.ArrayList;
import java.util.List;

public final class MovieData {
    private MovieData() {}

    public static List<Movie> nowShowing() {
        ArrayList<Movie> list = new ArrayList<>();
        list.add(new Movie(
                "The Dark Knight",
                "Action / 152 min",
                R.drawable.poster_dark_knight,
                "The Dark Knight trailer",
                false
        ));
        list.add(new Movie(
                "Inception",
                "Sci-Fi / 148 min",
                R.drawable.poster_inception,
                "Inception trailer",
                false
        ));
        list.add(new Movie(
                "Interstellar",
                "Sci-Fi / 169 min",
                R.drawable.poster_interstellar,
                "Interstellar trailer",
                false
        ));
        list.add(new Movie(
                "The Shawshank Redemption",
                "Drama / 142 min",
                R.drawable.poster_shawshank,
                "The Shawshank Redemption trailer",
                false
        ));
        return list;
    }

    public static List<Movie> comingSoon() {
        ArrayList<Movie> list = new ArrayList<>();
        list.add(new Movie(
                "Dune: Part Two",
                "Sci-Fi / 166 min",
                R.drawable.poster_inception,
                "Dune Part Two trailer",
                true
        ));
        list.add(new Movie(
                "Oppenheimer",
                "Drama / 180 min",
                R.drawable.poster_shawshank,
                "Oppenheimer trailer",
                true
        ));
        list.add(new Movie(
                "Avatar 3",
                "Adventure / 190 min",
                R.drawable.poster_interstellar,
                "Avatar 3 trailer",
                true
        ));
        return list;
    }
}

