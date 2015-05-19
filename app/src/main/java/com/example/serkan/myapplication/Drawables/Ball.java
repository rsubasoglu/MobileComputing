package com.example.serkan.myapplication.Drawables;

/**
 * Created on 01.04.2015.
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
        this.x = newX;//improveMoving();
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

    public int improveMoving() {
        if((newX - x) < 50)
            return x + 5;
        //else if(newX > (x+20))
        //    return x + 20;
        else if((x - newX) < 50)
            return x - 5;
        //else if((x-20) > newX)
        //    return x - 20;
        return newX;
    }
}
