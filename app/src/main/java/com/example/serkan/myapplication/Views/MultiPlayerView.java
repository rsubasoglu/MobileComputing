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
 * Created by Serkan on 10.05.2015.
 */
public class MultiPlayerView extends View {
    int framesPerSecond = 60;
    long animationDuration = 30; // 2 seconds
    long startTime;
    long elapsedTime = 0;

    Activity activity;

    // wurde die MultiPlayerView als server oder client gestartet
    private boolean isServer;
    // wurde ein neues balken erstellt
    private boolean isNewBalkAdded = false;

    // display coord. & balk size
    int MAX_X = 1080;
    int MAX_Y = 1920;
    int BALK_X = 300;
    int BALK_Y = 100;
    int newBalkPosX = 0;

    // views
    BallView ballView;
    BallView ballView2;
    BalkView balkView;

    // game variables
    boolean gameOver = false;
    int remoteBallX = 0;

    public MultiPlayerView(Context context, Activity activity, Object sensorService, boolean isServer) {
        super(context);
        this.activity = activity;
        this.isServer = isServer;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        ballView = new BallView(activity, sensorService, paint);

        Paint paint2 = new Paint();
        paint2.setColor(Color.GRAY);
        ballView2 = new BallView(activity, null, paint2);
        balkView = new BalkView(activity);

        FrameLayout fl = new FrameLayout(activity);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(lp);

        fl.addView(this);
        fl.addView(balkView);
        fl.addView(ballView2);
        fl.addView(ballView);

        activity.setContentView(fl);

        // start the animation
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // prüft ob eine collision stattfindet
        collision();
        // aktualisiere die position des remote balls
        ballView2.setRemoteBall(remoteBallX);

        if(elapsedTime > animationDuration && !gameOver) {
            // wenn server (und nicht client!)
            if(isServer) {
                // erstelle ein neues balken und speichere die position
                newBalkPosX = balkView.addNewRandomBalk();
                // ein neues balken erstellt
                isNewBalkAdded = true;
            }
            elapsedTime = 0;
        }
        elapsedTime++;
        this.postInvalidateDelayed(1000 / framesPerSecond);
    }

    /**
     * prüft ob eine collision stattgefunden hat
     */
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

    /**
     * setzt die x position des remote balls
     * @param x die position des remote balls
     */
    public void setRemoteBallX(int x) {
        this.remoteBallX = x;
    }

    /**
     * gibt die x position des balls
     * @return position des balls
     */
    public int getLocalBallX() {
        return ballView.getBallX();
    }

    /**
     * prüft ob ein neues balken erstellt wurde
     * @return
     */
    public boolean isNewBalkAdded() {
        return isNewBalkAdded;
    }

    /**
     * gibt die x position des neu erstellten balkens
     * @return position des neu erstellten balkens
     */
    public int getNewBalkPosX() {
        isNewBalkAdded = false;
        return newBalkPosX;
    }

    /**
     * setzt die x position des neu erstellten balkens
     * @param x position des neu erstellten balkens
     */
    public void setLocalNewBalkPosX(int x) {
        balkView.addNewBalk(x);
    }
}
