package com.example.test.dto;

import android.graphics.Bitmap;

public class Plane extends Root {

    private int live = 5;
    private Long time = 2000l;

    public Plane(float x, float y, Bitmap bitmap, int live, Integer bitmapType) {
        super(x, y, bitmap, bitmapType);
        this.live = live;
    }

    public Plane(float x, float y, Bitmap bitmap, Integer bitmapType) {
        super(x, y, bitmap, bitmapType);
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
                '}'
                + "," +
                "y{" +
                "bitmapType=" + this.getBitmapType() +
                '}';
    }
}
