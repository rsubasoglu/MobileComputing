package com.example.serkan.myapplication;

import android.util.Log;

/**
 * Created by Serkan on 01.04.2015.
 * Test by Ahmed
 */
public class Ball {

    private int x;
    private int newX;
    private int y;
    private float r; // radius

    public Ball(int x, int y, float r) {
        this.x = x;
        this.newX = x;
        this.y = y;
        this.r = r;
    }

    public void setX(int x) {
        this.newX = x;
        this.x = improveMoving();
    }

    public int getX() {
        return x;
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

    public int improveMoving() {
        if(newX > (x+50))
            return x + 50;
        //else if(newX > (x+20))
        //    return x + 20;
        else if((x-50) > newX)
            return x - 50;
        //else if((x-20) > newX)
        //    return x - 20;
        return newX;
    }
}
