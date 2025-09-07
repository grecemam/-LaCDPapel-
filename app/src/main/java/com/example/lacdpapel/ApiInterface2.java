package com.example.lacdpapel;
import com.example.lacdpapel.Models.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiInterface2 {
    @GET("v1.4/movie/{id}")
    Call<Movies> getMovie(@Header("X-API-KEY") String API_key, @Path("id") int id);
}
