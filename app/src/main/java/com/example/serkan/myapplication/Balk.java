package com.example.serkan.myapplication;

/**
 * Created by Serkan on 29.03.2015.
 */
public class Balk {
    private int X;
    private int Y;

    public Balk(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void goDown() {
        Y += 10;
    }
}
