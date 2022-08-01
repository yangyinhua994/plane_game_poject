package com.example.plane.dto;

public class User {

    private int ranking;
    private String username;
    private String number;
    private String time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User(int ranking, String username, String number, String time) {
        this.ranking = ranking;
        this.username = username;
        this.number = number;
        this.time = time;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "User{" +
                "ranking=" + ranking +
                ", username='" + username + '\'' +
                ", number='" + number + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
