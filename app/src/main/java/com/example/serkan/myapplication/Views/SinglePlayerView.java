package com.example.serkan.myapplication.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Serkan on 07.05.2015.
 */
public class SinglePlayerView extends View{
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

        ballView = new BallView(activity, sensorService);
        balkView = new BalkView(activity);

        FrameLayout fl = new FrameLayout(activity);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(lp);

        fl.addView(this.);
        fl.addView(balkView);
        fl.addView(ballView);

        activity.setContentView(fl);
    }

    @Override
    public void onDraw(Canvas canvas) {
        collision();
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
}
