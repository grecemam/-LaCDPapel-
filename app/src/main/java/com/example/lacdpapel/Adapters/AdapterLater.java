package com.example.lacdpapel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lacdpapel.Models.Films;
import com.example.lacdpapel.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class AdapterLater extends RecyclerView.Adapter<AdapterLater.ViewHolder> {

    private Context context;
    private List<Films> filmsList;


    public AdapterLater(Context context, List<Films> filmsList) {
        this.context = context;
        this.filmsList = filmsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_groups, parent, false);
        return new AdapterLater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLater.ViewHolder holder, int position) {
        Films films = filmsList.get(position);
        Picasso.with(context).load(films.getPosterUrl()).into(holder.iv);

        holder.nameRU.setText(films.getNameRu());

        if (Objects.equals(films.getNameRu(), "")) {
            holder.nameRU.setText(films.getNameEn());
        } else {
            holder.nameRU.setText(films.getNameRu());
        }

    }

    @Override
    public int getItemCount() {
        return filmsList.size();
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
