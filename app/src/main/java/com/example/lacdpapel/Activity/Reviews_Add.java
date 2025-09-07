package com.example.lacdpapel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lacdpapel.ApiInterface;
import com.example.lacdpapel.Models.Films;
import com.example.lacdpapel.Models.Review;
import com.example.lacdpapel.R;
import com.example.lacdpapel.ServiceBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reviews_Add extends AppCompatActivity {
    ApiInterface apiInterface;
    String apiKey;
    EditText gradeEV, kommentEV;
    TextView email;
    ProgressBar progressBar;
    ImageView actorPosterImageView;
    Button add_otziv;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference otzivRef;
    Integer filmId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otziv_add);
        filmId = getIntent().getIntExtra("filmId", 0);
        apiInterface = ServiceBuilder.buildRequest().create(ApiInterface.class);
        apiKey = "ae974634-294f-4e55-a856-e249334bb9e5";
        progressBar = findViewById(R.id.progressBar);
        actorPosterImageView = findViewById(R.id.actor_poster);
        add_otziv = findViewById(R.id.add_otziv_btn);
        email = findViewById(R.id.name_user);
        gradeEV = findViewById(R.id.quantity_ev);
        kommentEV = findViewById(R.id.Komment);
        loadFilmDetails(filmId);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            email.setText(userEmail);
            otzivRef = db.collection("reviews");
        }


        add_otziv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addReviewsToFirestore();

            }
        });


    }
    private void addReviewsToFirestore() {
        int grade = Integer.parseInt(gradeEV.getText().toString().trim());
        String komment = kommentEV.getText().toString().trim();

        if (grade == 0) {
            Toast.makeText(Reviews_Add.this, "Оценка начинается минимум с 1", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(komment.isEmpty())
        {
            Toast.makeText(Reviews_Add.this, "Комментарий не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        } else if (grade > 10) {
            Toast.makeText(Reviews_Add.this, "Оценка максимум 10", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firebaseAuth.getCurrentUser() != null) {

            Review review = new Review(null, grade, komment, email.getText().toString(), filmId);

            otzivRef.add(review)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String newRevId = documentReference.getId();

                            review.setOtziv_id(newRevId);

                            otzivRef.document(newRevId).set(review)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Reviews_Add.this, "Отзыв добавлен", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Reviews_Add.this, Reviews.class);
                                            intent.putExtra("filmId", filmId);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Reviews_Add.this, "Ошибка обновления отзыва", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Reviews_Add.this, "Ошибка добавления отзыва", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

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

                        Picasso.with(Reviews_Add.this)
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
        Intent intent = new Intent(Reviews_Add.this, DetailActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}

