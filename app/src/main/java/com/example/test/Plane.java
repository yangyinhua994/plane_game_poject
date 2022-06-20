package com.example.test;

import android.graphics.Bitmap;

public class Plane extends Root {

    private int live = 5;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    private Long time = 2000l;

    public Plane(float x, float y, Bitmap bitmap, int live) {
        super(x, y, bitmap);
        this.live = live;
    }

    public Plane(float x, float y, Bitmap bitmap) {
        super(x, y, bitmap);
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "live=" + live +
                '}' + "," +
                "x{" +
                "x=" + this.getX() +
                '}'
                + "," +
                "y{" +
                "y=" + this.getY() +
                '}';
    }
}
