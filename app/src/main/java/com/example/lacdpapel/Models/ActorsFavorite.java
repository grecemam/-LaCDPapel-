package com.example.lacdpapel.Models;

public class ActorsFavorite {
    private String userId;
    private String favid;
    private Integer ActorId;
    private String ActorName;
    public ActorsFavorite(String userId, String favid, Integer actorId, String actorName, String posterUrl) {
        this.userId = userId;
        this.favid = favid;
        ActorId = actorId;
        ActorName = actorName;
        PosterUrl = posterUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFavid() {
        return favid;
    }

    public void setFavid(String favid) {
        this.favid = favid;
    }

    public Integer getActorId() {
        return ActorId;
    }

    public void setActorId(Integer actorId) {
        ActorId = actorId;
    }

    public String getActorName() {
        return ActorName;
    }

    public void setActorName(String actorName) {
        ActorName = actorName;
    }

    public String getPosterUrl() {
        return PosterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        PosterUrl = posterUrl;
    }

    private String PosterUrl;



    public ActorsFavorite() {}





}
