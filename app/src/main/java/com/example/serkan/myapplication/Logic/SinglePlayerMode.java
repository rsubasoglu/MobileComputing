package com.example.serkan.myapplication.Logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.serkan.myapplication.R;
import com.example.serkan.myapplication.Views.BallView;
import com.example.serkan.myapplication.Views.BalkView;

/**
 * Created by Serkan on 07.05.2015.
 */
public class SinglePlayerMode {
    Activity activity;

    BallView ballView;
    BalkView balkView;

    public SinglePlayerMode(Activity activity, Object sensorService) {
        this.activity = activity;

        ballView = new BallView(activity, sensorService);
        balkView = new BalkView(activity);

        FrameLayout fl = new FrameLayout(activity);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(lp);

        fl.addView(balkView);
        fl.addView(ballView);

        activity.setContentView(fl);
    }

}
