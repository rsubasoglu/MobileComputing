package com.example.serkan.myapplication.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.example.serkan.myapplication.Drawables.Ball;
import com.example.serkan.myapplication.Sensors.AccelerometerSensor;

/**
 * Created on 07.05.2015.
 */
public class BallView extends View {
    int framesPerSecond = 60;
    long animationDuration = 30; // 2 seconds
    long startTime;
    long elapsedTime = 0;

    private Paint paint = new Paint();
    // sensor variable
    AccelerometerSensor accSensor;

    // display coord. & balk size
    private int MAX_X = 1080;
    private int MAX_Y = 1920;

    // ball
    private Ball ball;

    // game variables
    private boolean gameOver = true;

    public BallView(Context context, Object sensorService, Paint paint) {
        super(context);
        if(sensorService != null) {
            accSensor = new AccelerometerSensor(sensorService);
            gameOver = false;
        }
        // create ball
        this.ball = new Ball(MAX_X/2, 1500, 50);

        this.paint = paint;

        // start the animation
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // wenn spiel nicht beendet
        if(!gameOver) {
            // position des balls aktualisieren
            updateBallPosition(canvas);
        }
        // zeichnet den ball
        drawBall(canvas);
        this.postInvalidateDelayed(1000 / framesPerSecond);
    }

    /**
     * aktualisiert die position des balls
     * @param canvas
     */
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
        // set ball position
        ball.setX(coordX);
    }

    /**
     * zeichnet den ball
     * @param canvas
     */
    public void drawBall(Canvas canvas) {
        canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), paint);
    }

    /**
     * gibt die x position vom ball
     * @return position des balls
     */
    public int getBallX() {
        return ball.getX();
    }

    /**
     * gibt die y position vom ball
     * @return position des balls
     */
    public int getBallY() {
        return ball.getY();
    }

    /**
     * gibt den radius vom ball
     * @return raduis des balls
     */
    public float getBallR() {
        return ball.getR();
    }

    /**
     * spiel verloren
     * @param gameOver
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * aktualisiert die position des remote balls
     * @param x
     */
    public void setRemoteBall(int x) {
        ball.setX(x);
    }
}
