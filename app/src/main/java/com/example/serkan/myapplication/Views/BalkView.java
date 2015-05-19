package com.example.serkan.myapplication.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.serkan.myapplication.Drawables.Balk;

import java.util.Random;

/**
 * Created on 07.05.2015.
 */
public class BalkView extends View {
    Paint paint = new Paint();

    int framesPerSecond = 60;
    long animationDuration = 30; // 2 seconds
    long startTime;
    long elapsedTime = 0;

    // display coord. & balk size
    int MAX_X = 1080;
    int MAX_Y = 1920;
    int BALK_X = 300;
    int BALK_Y = 100;

    // balks
    Balk[] balks = new Balk[7];

    // game variables
    boolean gameOver = false;

    public BalkView(Context context) {
        super(context);
        // create temp balks
        for(int i = 0; i < balks.length; i++) {
            Balk b = new Balk(0, 1900);
            balks[i] = b;
        }

        // start the animation
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawBalks(canvas);
        if(elapsedTime > animationDuration && !gameOver) {
            addNewBalk();
            elapsedTime = 0;
        }
        elapsedTime++;
        this.postInvalidateDelayed(1000 / framesPerSecond);
    }

    public int randomBalkPositionX() {
        Random r = new Random();
        return r.nextInt((MAX_X - BALK_X));
    }

    public void addNewBalk() {
        for(int i = 0; i < balks.length; i++) {
            if(balks[i].getY() > MAX_Y) {
                int randomX = randomBalkPositionX();
                Balk b = new Balk(randomX, 0);
                balks[i] = b;
                break;
            }
        }
    }

    public void drawBalks(Canvas canvas) {
        for(int i = 0; i < balks.length; i++) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, balks[i].getY(), MAX_X, balks[i].getY()+BALK_Y, paint);
            paint.setColor(Color.RED);
            canvas.drawRect(balks[i].getX(), balks[i].getY(), balks[i].getX()+BALK_X, balks[i].getY()+BALK_Y, paint);
            if(!gameOver)
                balks[i].goDown();
        }
    }

    public int[] getBalkX() {
        int[] x = new int[balks.length];
        for(int i = 0; i < x.length; i++) {
            x[i] = balks[i].getX();
        }
        return x;
    }

    public int[] getBalkY() {
        int[] y = new int[balks.length];
        for(int i = 0; i < y.length; i++) {
            y[i] = balks[i].getY();
        }
        return y;
    }
}
