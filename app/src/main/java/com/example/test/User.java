package com.example.test;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.text.SimpleDateFormat;

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

    @NonNull
    @Override
    public String toString() {
        String data = "";
        if (ranking == 1){
            data = "      排名       用户名      分数        时间" + "\n" + "\n";
        }
        data += "        " + getRanking() + "         "+getUsername()+"   " + getNumber() + "            " +getTime();
        return data;
    }

    public String getString(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
        return dateFormat.format(date);
    }

}
