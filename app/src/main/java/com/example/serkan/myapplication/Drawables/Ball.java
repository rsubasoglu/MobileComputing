package com.example.serkan.myapplication.Drawables;

/**
 * Created on 01.04.2015.
 */
public class Ball {

    private int x;
    private int y;
    private float r; // radius

    public Ball(int x, int y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
