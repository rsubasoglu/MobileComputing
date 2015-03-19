package com.example.serkan.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;

/**
 * Created by Serkan on 19.03.2015.
 */
public class GameActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TableRow tr2 = (TableRow) findViewById(R.id.tableRow2);

        tr2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}