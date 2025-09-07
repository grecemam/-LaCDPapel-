package com.example.lacdpapel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Models.ActorsFavorite;
import com.example.lacdpapel.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFavoriteActor extends RecyclerView.Adapter<AdapterFavoriteActor.ViewHolder> {
    private Context context;
    private List<ActorsFavorite> actorsFavoriteList;

    public AdapterFavoriteActor(Context context, List<ActorsFavorite> actorsFavoriteList) {
        this.context = context;
        this.actorsFavoriteList = actorsFavoriteList;
    }

    @NonNull
    @Override
    public AdapterFavoriteActor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_groups, parent, false);
        return new AdapterFavoriteActor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavoriteActor.ViewHolder holder, int position) {
        ActorsFavorite actors = actorsFavoriteList.get(position);
        Picasso.with(context).load(actors.getPosterUrl()).into(holder.iv);
        holder.nameRU.setText(actors.getActorName());
    }

    @Override
    public int getItemCount() {
        return actorsFavoriteList.size();
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
