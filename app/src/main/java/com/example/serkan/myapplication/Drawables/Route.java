package com.example.serkan.myapplication.Drawables;

/**
 * Created on 29.03.2015.
 */
public class Route {
    private int X1;
    private int Y1;
    private int X2;
    private int Y2;

    public Route(int X1, int Y1, int X2, int Y2) {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
    }

    public int getX1() {
        return X1;
    }

    public int getY1() {
        return Y1;
    }

    public int getX2() {
        return X2;
    }

    public int getY2() {
        return Y2;
    }

    public void goUp(int Y) {
        Y1 -= Y;
        Y2 -= Y;
    }

    public void goDown(int Y) {
        Y1 += Y;
        Y2 += Y;
    }

    public void goLeft(int X) {
        X1 -= X;
        X2 -= X;
    }

    public void goRight(int X) {
        X1 += X;
        X2 += X;
    }
}
