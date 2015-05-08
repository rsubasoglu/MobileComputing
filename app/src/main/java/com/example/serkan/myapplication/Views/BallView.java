package com.example.serkan.myapplication.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.serkan.myapplication.Drawables.Ball;
import com.example.serkan.myapplication.Sensors.AccelerometerSensor;

/**
 * Created by Serkan on 07.05.2015.
 */
public class BallView extends View {
    Paint paint = new Paint();
    AccelerometerSensor accSensor;

    // display coord. & balk size
    int MAX_X = 1080;
    int MAX_Y = 1920;

    // ball
    Ball ball;

    // game variables
    boolean gameOver = false;

    public BallView(Context context, Object sensorService) {
        super(context);
        accSensor = new AccelerometerSensor(sensorService);

        // create ball
        ball = new Ball(MAX_X/2, 1500, 50);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(!gameOver) {
            updateBallPosition(canvas);
        }
        drawBall(canvas);
    }

    public void updateBallPosition(Canvas canvas) {
        // parameter x from accSensor:
        // 80 = left, 0 = center, -80 = right
        // get x parameter from sensor and convert it to percent
        float sensorX = accSensor.getX() * (-1);
        float sensorXPercent = (sensorX + 40) * 100 / (80);
        if(sensorXPercent > 100)
            sensorXPercent = 100;
        else if(sensorXPercent < 0)
            sensorXPercent = 0;
        // calculate the percent number in display coord.
        int coordX = (int)(sensorXPercent * MAX_X / 100);
        canvas.drawText(String.valueOf(coordX), MAX_X / 2, MAX_Y / 2, paint);
        //if((ball.getX() - coordX) > 10 || (coordX - ball.getX()) > 10)
        ball.setX(coordX);
    }

    public void drawBall(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), paint);
    }

    public int getBallX() {
        return ball.getX();
    }

    public int getBallY() {
        return ball.getY();
    }

    public float getBallR() {
        return ball.getR();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
