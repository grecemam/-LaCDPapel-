package com.example.lacdpapel.Models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Films {
    @SerializedName("kinopoiskId")
    private int kinopoiskId;
    @SerializedName("nameRu")
    private String nameRu;
    @SerializedName("nameEn")
    private String nameEn;
    @SerializedName("year")
    private int year;
    @SerializedName("nameOriginal")
    private String nameOriginal;
    @SerializedName("posterUrl")
    private String posterUrl;
    @SerializedName("posterUrlPreview")
    private String posterUrlPreview;
    @SerializedName("ratingKinopoisk")
    private double ratingKinopoisk;

    @SerializedName("description")
    private String description;
    @SerializedName("duration")
    private int duration;
    @SerializedName("premiereRu")
    private String premiereRu;
    @SerializedName("filmId")
    private int filmId;


    public int getKinopoiskId() {
        return kinopoiskId;
    }

    public String getNameOriginal() {
        return nameOriginal;
    }

    public String getNameRu() {
        return nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public double getRatingKinopoisk() {
        return ratingKinopoisk;
    }

    public int getYear() {
        return year;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getPosterUrlPreview() {
        return posterUrlPreview;
    }
    public String getDescription() {
        return description;
    }
    public int getDuration() {
        return duration;
    }
    public String getPremiereRu() {
        return premiereRu;
    }
    public int getFilmId()
    {
        return filmId;
    }

}

