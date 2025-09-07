package com.example.lacdpapel.Models;

public class Review {
    private String otziv_id;
    private Integer grade;
    public Review() {
    }

    public String getOtziv_id() {
        return otziv_id;
    }

    public void setOtziv_id(String otziv_id) {
        this.otziv_id = otziv_id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getKomment() {
        return komment;
    }

    public void setKomment(String komment) {
        this.komment = komment;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    private String komment;
    private String user_email;

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    private Integer filmId;

    public Review(String otziv_id, Integer grade, String komment, String user_email, Integer filmId) {
        this.otziv_id = otziv_id;
        this.grade = grade;
        this.komment = komment;
        this.user_email = user_email;
        this.filmId = filmId;
    }



}
