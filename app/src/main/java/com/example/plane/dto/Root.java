package com.example.plane.dto;

import android.graphics.Bitmap;

public class Root {

    private float x;
    private float y;
    private Bitmap bitmap;
    private Integer bitmapType = 1;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Integer getBitmapType() {
        return bitmapType;
    }

    public void setBitmapType(Integer bitmapType) {
        this.bitmapType = bitmapType;
    }

    public Root(float x, float y, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
    }

    public Root(float x, float y, Bitmap bitmap, Integer bitmapType) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        this.bitmapType = bitmapType;
    }
}
