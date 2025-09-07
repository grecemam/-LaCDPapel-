package com.example.lacdpapel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Adapters.AdapterLater;
import com.example.lacdpapel.ApiInterface;
import com.example.lacdpapel.Models.Films;
import com.example.lacdpapel.R;
import com.example.lacdpapel.ServiceBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookLater extends AppCompatActivity {
    RecyclerView recyclerView;
    ApiInterface apiInterface;
    ProgressBar progressBar;
    AdapterLater adapterLater;
    String apiKey;
    List<Integer> filmIdsList = new ArrayList<>();
    private List<Films> filmsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_later);
        recyclerView = findViewById(R.id.recyclerFilmsLater);
        apiInterface = ServiceBuilder.buildRequest().create(ApiInterface.class);
        apiKey = "9e0a4403-ffba-486d-988d-dac5e7f1431a";
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference filmsAfterCollection = FirebaseFirestore.getInstance().collection("FilmsAfter");

        filmsAfterCollection.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Long filmIdLong = document.getLong("filmIdApi");
                        if (filmIdLong != null) {
                            int filmId = filmIdLong.intValue();
                            filmIdsList.add(filmId);
                        }
                    }
                    LoadFilms(filmIdsList);
                } else {
                    Toast.makeText(LookLater.this, "Ошибка при получении данных", Toast.LENGTH_SHORT).show();
                }

            }

        });



    }

    private void LoadFilms(List<Integer> filmIdsList) {
        for (int filmId : filmIdsList) {
            Call<Films> getFilmCall = apiInterface.getFilm(apiKey, filmId);

            getFilmCall.enqueue(new Callback<Films>() {
                @Override
                public void onResponse(Call<Films> call, Response<Films> response) {
                    if (response.isSuccessful()) {
                        Films film = response.body();
                        if (film != null) {
                            filmsList.add(film);
                            initializeAdapter();
                        }
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("API Error", errorBody);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(LookLater.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Films> call, Throwable t) {
                    Log.e("API Error", t.getMessage());
                    Toast.makeText(LookLater.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void initializeAdapter() {
        adapterLater = new AdapterLater(LookLater.this, filmsList);
        recyclerView.setLayoutManager(new GridLayoutManager(LookLater.this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterLater);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LookLater.this, glavnaya.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
