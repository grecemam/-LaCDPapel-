package com.example.lacdpapel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Models.Review;
import com.example.lacdpapel.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.ViewHolder> {
    private Context context;
    private List<Review> reviewList = new ArrayList<Review>();
    public AdapterReviews(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }
    @NonNull
    @Override
    public AdapterReviews.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reviews, parent, false);
        return new AdapterReviews.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterReviews.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.grade.setText(String.valueOf(review.getGrade()));
        holder.user_email.setText(review.getUser_email());
        holder.komment.setText(review.getKomment());
    }
    @Override
    public int getItemCount() {
        return reviewList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView grade, user_email, komment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            grade = itemView.findViewById(R.id.grade);
            user_email = itemView.findViewById(R.id.user_email);
            komment = itemView.findViewById(R.id.Komment);
        }
    }
}