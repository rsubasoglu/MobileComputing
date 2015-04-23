package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.serkan.myapplication.R;

/**
 * Created by Serkan on 19.03.2015.
 */
public class RankingActivity extends Activity implements AdapterView.OnItemClickListener {
    //public static final String Highscore = "HIGHSCORE";
    private ListView ScoreListView;
    private String[] score = new String[] {"2", "12",
            "2"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Button backButton = (Button) findViewById(R.id.backButton);

        Intent inIntent = getIntent();

        // Referenz auf die View besorgen
        ScoreListView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, score);
        ScoreListView.setAdapter(adapter);
        ScoreListView.setOnItemClickListener(this);
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "Score " + score[i] + " ausgewählt!",
                Toast.LENGTH_SHORT).show();
    }
}
