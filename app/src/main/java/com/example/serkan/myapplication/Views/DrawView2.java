package com.example.serkan.myapplication.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.serkan.myapplication.Drawables.Route;
import com.example.serkan.myapplication.Drawables.Ball;
import com.example.serkan.myapplication.Sensors.AccelerometerSensor;

import java.util.Random;

/**
 * Created by Serkan on 29.03.2015.
 */
public class DrawView2 extends View {
    Paint paint = new Paint();
    AccelerometerSensor accSensor;

    int framesPerSecond = 60;
    long animationDuration = 30; // 2 seconds
    long startTime;
    long elapsedTime = 0;

    // display coord. & route size
    int MAX_X = 1080;
    int MAX_Y = 1920;
    int ROUTE_WIDTH = 300;

    // balks
    Route[] route = new Route[10];

    // ball
    Ball ball;

    // game variables
    boolean gameOver = false;
    int points = 0;

    public DrawView2(Context context, Object sensorService) {
        super(context);
        accSensor = new AccelerometerSensor(sensorService);
        // create temp route
        int x1 = MAX_X/2+ROUTE_WIDTH/2;
        for(int i = 0; i < route.length; i++) {
            Route r = new Route(x1, 0, x1 + ROUTE_WIDTH, MAX_Y);
            route[i] = r;
        }

        // create ball
        ball = new Ball(MAX_X/2, MAX_Y/2, 50);

        // start the animation
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        //collision();
        drawRoute(canvas);
        if(!gameOver)
            updateRoutePosition();
        drawBall(canvas);
        drawText(canvas);

        if(elapsedTime > animationDuration && !gameOver) {
            //addNewRoute();
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

    /*
    public int randomRoutePositionX() {
        Random r = new Random();
        return r.nextInt((MAX_X - BALK_X));
    }
    */

    /*
    public void addNewRoute() {
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
    */

    public void drawRoute(Canvas canvas) {
        paint.setColor(Color.RED);
        for(int i = 0; i < route.length; i++) {
            canvas.drawRect(route[i].getX1(), route[i].getY1(), route[i].getX2(), route[i].getY2(), paint);
        }
    }

    public void updateRoutePosition() {
        // parameter x from accSensor:
        // 10 = left, 0 = center, -10 = right
        // get x parameter from sensor and convert it to percent
        float sensorX = accSensor.getX() * (-1);
        float sensorY = accSensor.getY();

        // calculate x
        float sensorXPercent = (sensorX + 40) * 100 / (80);
        if(sensorXPercent > 100)
            sensorXPercent = 100;
        else if(sensorXPercent < 0)
            sensorXPercent = 0;
        // calculate the percent number in display coord.
        int coordX = (int)(sensorXPercent * MAX_X / 100);

        // calculate y
        float sensorYPercent = (sensorY + 40) * 100 / (80);
        if(sensorYPercent > 100)
            sensorYPercent = 100;
        else if(sensorYPercent < 0)
            sensorYPercent = 0;
        // calculate the percent number in display coord.
        int coordY = (int)(sensorYPercent * MAX_Y / 100);

        coordX /= 50;
        coordY /= 50;
        for(int i = 0; i < route.length; i++) {
            if(sensorX > 0)
                route[i].goRight(coordX);
            else route[i].goLeft(coordX);
            if(sensorY > 0)
                route[i].goUp(coordY);
            else route[i].goDown(coordY);
        }
    }

    public void drawBall(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), paint);
    }

    /*
    public void collision() {
        for(int i = 0; i < balks.length; i++) {
            if(balks[i].getY()+BALK_Y >= ball.getY()-ball.getR() && balks[i].getY() <= ball.getY()+ball.getR()) {
                if(ball.getX()+ball.getR() >= balks[i].getX() && ball.getX()-ball.getR() <= balks[i].getX()+BALK_X) {
                    //gameOver = true;
                }
            }
        }
    }
    */
}
