package com.example.lacdpapel.Adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lacdpapel.Models.Movies;
import com.example.lacdpapel.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterFilmsActors extends RecyclerView.Adapter<AdapterFilmsActors.ViewHolder> {
    private Context context;
    private List<Movies> moviesList;

    public AdapterFilmsActors(Context context, List<Movies> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_films_actor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movies movie = moviesList.get(position);
        Picasso.with(context).load(movie.getPoster().getUrl()).into(holder.iv);
        holder.nameRU.setText(movie.getName());
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView nameRU;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.groupsImg);
            nameRU = itemView.findViewById(R.id.nameGroupRU);
        }
    }
}
