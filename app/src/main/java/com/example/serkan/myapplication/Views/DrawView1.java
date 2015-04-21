package com.example.serkan.myapplication.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.serkan.myapplication.Drawables.Balk;
import com.example.serkan.myapplication.Drawables.Ball;
import com.example.serkan.myapplication.Sensors.AccelerometerSensor;

import java.util.Random;

/**
 * Created by Serkan on 29.03.2015.
 */
public class DrawView1 extends View {
    Paint paint = new Paint();
    AccelerometerSensor accSensor;

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

    // ball
    Ball ball;

    // game variables
    boolean gameOver = false;
    int points = 0;

    public DrawView1(Context context, Object sensorService) {
        super(context);
        accSensor = new AccelerometerSensor(sensorService);
        // create temp balks
        for(int i = 0; i < balks.length; i++) {
            Balk b = new Balk(0, 1900);
            balks[i] = b;
        }

        // create ball
        ball = new Ball(MAX_X/2, 1500, 50);

        // start the animation
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        collision();
        drawBalks(canvas);
        if(!gameOver)
            updateBallPosition(canvas);
        drawBall(canvas);
        drawText(canvas);

        if(elapsedTime > animationDuration && !gameOver) {
            addNewBalk();
            elapsedTime = 0;
        }

        elapsedTime++;
        this.postInvalidateDelayed(1000 / framesPerSecond);
    }

    private void drawText(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(80);
        canvas.drawText(String.valueOf(points), 0, String.valueOf(points).length(), 20, 80, paint);
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
        points++;
    }

    public void drawBalks(Canvas canvas) {
        paint.setColor(Color.RED);
        for(int i = 0; i < balks.length; i++) {
            canvas.drawRect(balks[i].getX(), balks[i].getY(), balks[i].getX()+BALK_X, balks[i].getY()+BALK_Y, paint);
            if(!gameOver)
                balks[i].goDown();
        }
    }

    public void updateBallPosition(Canvas canvas) {
        // parameter x from accSensor:
        // 80 = left, 0 = center, -80 = right
        // get x parameter from sensor and convert it to percent
        float sensorX = accSensor.getX() * (-1);
        float sensorXPercent = (sensorX + 80) * 100 / (160);
        if(sensorXPercent > 100)
            sensorXPercent = 100;
        else if(sensorXPercent < 0)
            sensorXPercent = 0;
        // calculate the percent number in display coord.
        int coordX = (int)(sensorXPercent * MAX_X / 100);
        canvas.drawText(String.valueOf(coordX), MAX_X/2, MAX_Y/2, paint);
        //if((ball.getX() - coordX) > 10 || (coordX - ball.getX()) > 10)
            ball.setX(coordX);
    }

    public void drawBall(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), paint);
    }

    public void collision() {
        for(int i = 0; i < balks.length; i++) {
            if(balks[i].getY()+BALK_Y >= ball.getY()-ball.getR() && balks[i].getY() <= ball.getY()+ball.getR()) {
                if(ball.getX()+ball.getR() >= balks[i].getX() && ball.getX()-ball.getR() <= balks[i].getX()+BALK_X) {
                    gameOver = true;
                }
            }
        }
    }
}
