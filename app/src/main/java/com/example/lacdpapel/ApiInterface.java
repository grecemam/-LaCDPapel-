package com.example.lacdpapel;
import com.example.lacdpapel.Models.Actors;
import com.example.lacdpapel.Models.Films;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("api/v2.2/films/{id}")
    Call<Films> getFilm(@Header("X-API-KEY") String API_key, @Path("id") int id);

    @GET("api/v1/staff")
    Call<List<Actors>> getActors(@Header("X-API-KEY") String API_key, @Query("filmId") int filmId);

    @GET("api/v1/staff/{id}")
    Call<Actors> getStaffDetails(@Header("X-API-KEY") String apiKey, @Path("id") int id);



}
