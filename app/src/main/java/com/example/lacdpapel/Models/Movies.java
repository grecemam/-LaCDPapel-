package com.example.lacdpapel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movies {
    @SerializedName("id")
    private int id;

    @SerializedName("collections")
    private List<Object> collections;



    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("facts")
    private List<Object> facts;

    @SerializedName("name")
    private String name;

    @SerializedName("poster")
    private Poster poster;

    public int getId() {
        return id;
    }

    public List<Object> getCollections() {
        return collections;
    }



    public String getCreatedAt() {
        return createdAt;
    }

    public List<Object> getFacts() {
        return facts;
    }

    public String getName() {
        return name;
    }

    public Poster getPoster() {
        return poster;
    }

}

