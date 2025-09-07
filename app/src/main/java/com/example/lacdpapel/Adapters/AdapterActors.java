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
import java.util.List;

public class AdapterActors extends RecyclerView.Adapter<AdapterActors.ViewHolder> {
    private Context context;
    private List<Actors> actorsList;
    private OnItemClickListener onItemClickListener;

    public AdapterActors(Context context, List<Actors> actorsList) {
        this.context = context;
        this.actorsList = actorsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_actot, parent, false);
        return new ViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(int actorId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Actors actors = actorsList.get(position);
        Picasso.with(context).load(actors.getPosterUrl()).into(holder.iv);
        holder.nameRU.setText(actors.getNameRu());
        holder.descr.setText(actors.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(actors.getStaffId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return actorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView nameRU, descr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.actorsImg);
            nameRU = itemView.findViewById(R.id.nameActorRU);
            descr = itemView.findViewById(R.id.nameActorDescr);
        }
    }
}

