package com.example.lacdpapel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Models.Actors;
import com.example.lacdpapel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdapterSGroup extends RecyclerView.Adapter<AdapterSGroup.ViewHolder> {
    private Context context;
    private List<Actors> actorsList = new ArrayList<Actors>();

    public AdapterSGroup(Context context, List<Actors> actorsList) {
        this.context = context;
        this.actorsList = actorsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_sgroup, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Actors actors = actorsList.get(position);
        Picasso.with(context).load(actors.getPosterUrl()).into(holder.iv);
        if(Objects.equals(actors.getNameRu(), "")){
            holder.nameRU.setText(actors.getNameEn());
        }else{
            holder.nameRU.setText(actors.getNameRu());
        }
        if(Objects.equals(actors.getDescription(), null)){
            holder.descr.setText("Продюсер");
        }else{
            holder.descr.setText(actors.getDescription());
        }
    }



    @Override
    public int getItemCount() {
        return actorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView nameRU, descr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.groupsImg);
            nameRU = itemView.findViewById(R.id.nameGroupRU);
            descr = itemView.findViewById(R.id.nameGroupDescr);
        }
    }

}