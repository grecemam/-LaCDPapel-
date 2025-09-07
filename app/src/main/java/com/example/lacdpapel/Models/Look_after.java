package com.example.lacdpapel.Models;

public class Look_after {

    private String userId;
    private String lookId;
    private Integer filmIdApi;

    public Look_after() {}


    public Look_after(String userId, Integer filmIdApi, String lookId) {
        this.userId = userId;
        this.filmIdApi = filmIdApi;
        this.lookId = lookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getFilmIdApi() {
        return filmIdApi;
    }

    public void setFilmIdApi(Integer filmIdApi) {
        this.filmIdApi = filmIdApi;
    }
    public String getLookId() {
        return lookId;
    }

    public void setLookId(String lookId) {
        this.lookId = lookId;
    }
}
