package com.example.serkan.myapplication.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
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

    // display coord. & balk size
    private int MAX_X = 1080;
    private int MAX_Y = 1920;

    // ball
    private Ball ball;

    // game variables
    private boolean gameOver = true;

    public BallView(Context context, SensorManager sensorService, Paint paint) {
        super(context);
        if(sensorService != null) {
            // create ball
            this.ball = new Ball(MAX_X/2, 1500, 50, sensorService);
            gameOver = false;
        }
        else {
            this.ball = new Ball(MAX_X/2, 1500, 50, null);
        }

        this.paint = paint;

        // start the animation
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // wenn spiel nicht beendet
        if(!gameOver) {
            // hier spiel stoppen
        }
        // zeichnet den ball
        drawBall(canvas);
        this.postInvalidateDelayed(1000 / framesPerSecond);
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
