package com.cinefast.app;

public class Movie {
    private final String name;
    private final String subtitle;
    private final int posterResId;
    private final String trailerQuery;
    private final boolean comingSoon;

    public Movie(String name, String subtitle, int posterResId, String trailerQuery, boolean comingSoon) {
        this.name = name;
        this.subtitle = subtitle;
        this.posterResId = posterResId;
        this.trailerQuery = trailerQuery;
        this.comingSoon = comingSoon;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getPosterResId() {
        return posterResId;
    }

    public String getTrailerQuery() {
        return trailerQuery;
    }

    public boolean isComingSoon() {
        return comingSoon;
    }
}

