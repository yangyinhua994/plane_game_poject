package com.example.plane.dto;

import android.graphics.Bitmap;

public class Plane extends Root {

    private int live = 5;
    private int moveSpeed = 0;

    public Plane(float x, float y, Bitmap bitmap, int live, Integer bitmapType) {
        super(x, y, bitmap, bitmapType);
        this.live = live;
    }

    public Plane(float x, float y, Bitmap bitmap, Integer bitmapType) {
        super(x, y, bitmap, bitmapType);
    }

    public Plane(float x, float y, Bitmap bitmap, int live, Integer bitmapType, int moveSpeed) {
        super(x, y, bitmap, bitmapType);
        this.live = live;
        this.moveSpeed = moveSpeed;
    }


    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public int getMoveSpeed() {
        return moveSpeed;
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
