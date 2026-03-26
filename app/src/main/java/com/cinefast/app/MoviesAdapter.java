package com.cinefast.app;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieVH> {

    public static final String EXTRA_MOVIE_TYPE = "movie_type";
    public static final String MOVIE_TYPE_NOW_SHOWING = "now_showing";
    public static final String MOVIE_TYPE_COMING_SOON = "coming_soon";

    private static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=";

    private final List<Movie> movies;

    public MoviesAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        Movie movie = movies.get(position);
        holder.tvName.setText(movie.getName());
        holder.tvSubtitle.setText(movie.getSubtitle());
        holder.ivPoster.setImageResource(movie.getPosterResId());

        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(YOUTUBE_SEARCH_URL + Uri.encode(movie.getTrailerQuery())));
            if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                v.getContext().startActivity(intent);
            }
        });

        holder.btnBookSeats.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SeatSelectionActivity.class);
            intent.putExtra(SeatSelectionActivity.EXTRA_MOVIE_NAME, movie.getName());
            intent.putExtra(EXTRA_MOVIE_TYPE,
                    movie.isComingSoon() ? MOVIE_TYPE_COMING_SOON : MOVIE_TYPE_NOW_SHOWING);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieVH extends RecyclerView.ViewHolder {
        final ImageView ivPoster;
        final TextView tvName;
        final TextView tvSubtitle;
        final Button btnBookSeats;
        final Button btnTrailer;

        MovieVH(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvName = itemView.findViewById(R.id.tvMovieName);
            tvSubtitle = itemView.findViewById(R.id.tvMovieSubtitle);
            btnBookSeats = itemView.findViewById(R.id.btnBookSeats);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
        }
    }
}

