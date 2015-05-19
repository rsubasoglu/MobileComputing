package com.example.serkan.myapplication.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created on 07.05.2015.
 */
public class SinglePlayerView extends View{
    int framesPerSecond = 60;
    long animationDuration = 30; // 2 seconds
    long startTime;
    long elapsedTime = 0;

    Activity activity;

    // display coord. & balk size
    int MAX_X = 1080;
    int MAX_Y = 1920;
    int BALK_X = 300;
    int BALK_Y = 100;

    // views
    BallView ballView;
    BalkView balkView;

    // game variables
    boolean gameOver = false;

    public SinglePlayerView(Context context, Activity activity, Object sensorService) {
        super(context);
        this.activity = activity;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        ballView = new BallView(activity, sensorService, paint);
        balkView = new BalkView(activity);

        FrameLayout fl = new FrameLayout(activity);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(lp);

        fl.addView(this);
        fl.addView(balkView);
        fl.addView(ballView);

        activity.setContentView(fl);

        // start the animation
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        collision();

        if(elapsedTime > animationDuration && !gameOver) {

            elapsedTime = 0;
        }
        elapsedTime++;
        this.postInvalidateDelayed(1000 / framesPerSecond);
    }

    public void collision() {
        for(int i = 0; i < balkView.getBalkX().length; i++) {
            if(balkView.getBalkX()[i]+BALK_Y >= ballView.getBallY()-ballView.getBallR() && balkView.getBalkY()[i] <= ballView.getBallY()+ballView.getBallR()) {
                if(ballView.getBallX()+ballView.getBallR() >= ballView.getBallX() && ballView.getBallX()-ballView.getBallR() <= balkView.getBalkX()[i]+BALK_X) {
                    gameOver = true;
                    ballView.setGameOver(true);
                }
            }
        }
    }

    public int getBallX() {
        return ballView.getBallX();
    }
}
