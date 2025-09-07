package com.example.lacdpapel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Adapters.AdapterReviews;
import com.example.lacdpapel.ApiInterface;
import com.example.lacdpapel.Models.Films;
import com.example.lacdpapel.Models.Review;
import com.example.lacdpapel.R;
import com.example.lacdpapel.ServiceBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Reviews extends AppCompatActivity {
    ApiInterface apiInterface;
    String apiKey;
    ProgressBar progressBar;
    Integer filmId;
    ImageView actorPosterImageView, back_iv, plusAdd;
    RecyclerView otzivi_recyclerView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reviewsRef;
    private AdapterReviews adapterReviews;
    List<Review> reviewList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otzivi);
        filmId = getIntent().getIntExtra("filmId", 0);
        apiInterface = ServiceBuilder.buildRequest().create(ApiInterface.class);
        apiKey = "ae974634-294f-4e55-a856-e249334bb9e5";
        progressBar = findViewById(R.id.progressBar);
        actorPosterImageView = findViewById(R.id.actor_poster);
        otzivi_recyclerView = findViewById(R.id.recyclerOtzivi);
        reviewList =new ArrayList<>();


        if (firebaseAuth.getCurrentUser() != null) {
            reviewsRef = db.collection("reviews");
        }
        LoadReviews();
        plusAdd = findViewById(R.id.plusIcon);
        plusAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reviews.this, Reviews_Add.class);
                intent.putExtra("filmId", filmId);
                startActivity(intent);
            }
        });

        loadFilmDetails(filmId);


    }

    private void LoadReviews() {
        reviewsRef.whereEqualTo("filmId", filmId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) { return; }
                        reviewList.clear();
                        for (QueryDocumentSnapshot document : snapshot) {
                            Review review = document.toObject(Review.class);
                            reviewList.add(review);
                            updateReviews();
                        }
                        adapterReviews.notifyDataSetChanged();
                    }
                });
    }

    private void updateReviews() {
        adapterReviews = new AdapterReviews(Reviews.this, reviewList);
        otzivi_recyclerView.setLayoutManager(new GridLayoutManager(Reviews.this, 1, LinearLayoutManager.VERTICAL, false));
        otzivi_recyclerView.setHasFixedSize(true);
        otzivi_recyclerView.setAdapter(adapterReviews);
    }

    private void loadFilmDetails(int filmId) {
        progressBar.setVisibility(View.VISIBLE);

        Call<Films> getFilmCall = apiInterface.getFilm(apiKey, filmId);
        getFilmCall.enqueue(new Callback<Films>() {
            @Override
            public void onResponse(Call<Films> call, Response<Films> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Films film = response.body();
                    if (film != null && film.getPosterUrl() != null) {
                        Picasso.with(Reviews.this)
                                .load(film.getPosterUrl())
                                .into(actorPosterImageView);
                    }
                } else {
                    if (response.errorBody() != null) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Films> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Reviews.this, DetailActivity.class);
        intent.putExtra("filmId", filmId);
        startActivity(intent);
        super.onBackPressed();
    }
}
