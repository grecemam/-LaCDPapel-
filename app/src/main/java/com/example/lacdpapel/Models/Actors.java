package com.example.lacdpapel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Actors {
    @SerializedName("staffId")
    private int staffId;
    @SerializedName("nameRu")
    private String nameRu;
    @SerializedName("nameEn")
    private String nameEn;
    @SerializedName("description")
    private String description;
    @SerializedName("posterUrl")
    private String posterUrl;
    @SerializedName("professionKey")
    private String professionKey;
    @SerializedName("professionText")
    private String professionText;
    @SerializedName("growth")
    private String growth;
    @SerializedName("profession")
    private String profession;
    @SerializedName("birthday")
    private String birthday;

    @SerializedName("personId")
    private int personId;
    @SerializedName("age")
    private int age;
    @SerializedName("films")
    private List<Films> films;

    @SerializedName("filmId")
    private int filmId;







    public int getStaffId() {
        return staffId;
    }

    public String getNameRu() {
        return nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getProfessionKey() {
        return professionKey;
    }

    public String getProfessionText() {
        return professionText;
    }

    public String getProfession() { return profession; }
    public String getGrowth() { return growth; }
    public String getBirthday() { return birthday; }
    public int getPersonId() { return personId; }
    public int getAge() { return age; }

    public List<Films> getFilms() {
        return films;
    }

    public int getFilmId()
    {
        return filmId;
    }
}