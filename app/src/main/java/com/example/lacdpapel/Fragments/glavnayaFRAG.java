package com.example.lacdpapel.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Activity.DetailActivity;
import com.example.lacdpapel.Adapters.AdapterGroups;
import com.example.lacdpapel.ApiInterface;
import com.example.lacdpapel.Models.Films;
import com.example.lacdpapel.R;
import com.example.lacdpapel.ServiceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class glavnayaFRAG extends Fragment {
    RecyclerView recyclerView;
    ApiInterface apiInterface;
    ProgressBar progressBar;
    AdapterGroups adapterGroups;
    ImageView img;

    private List<Films> filmsList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_glavnaya, container, false);
        recyclerView = root.findViewById(R.id.photos);
        img = root.findViewById(R.id.imageView2);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("filmId", 1046206);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ServiceBuilder.buildRequest().create(ApiInterface.class);
        String apiKey = "9e0a4403-ffba-486d-988d-dac5e7f1431a";

        List<Integer> filmIds = Arrays.asList(1117735,915196, 4795888);
        for (int filmId : filmIds) {
            Call<Films> getFilmCall = apiInterface.getFilm(apiKey, filmId);

            getFilmCall.enqueue(new Callback<Films>() {
                @Override
                public void onResponse(Call<Films> call, Response<Films> response) {
                    if (response.isSuccessful()) {
                        Films film = response.body();
                        if (film != null) {
                            filmsList.add(film);
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
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }

                    if (filmsList.size() == filmIds.size()) {
                        initializeAdapter();
                    }
                }

                @Override
                public void onFailure(Call<Films> call, Throwable t) {
                    Log.e("API Error", t.getMessage());
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeAdapter() {
        adapterGroups = new AdapterGroups(getContext(), filmsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterGroups);

        adapterGroups.setOnItemClickListener(new AdapterGroups.OnItemClickListener() {
            @Override
            public void onItemClick(int filmId) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("filmId", filmId);
                startActivity(intent);
            }
        });
    }


}

