package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.serkan.myapplication.Database.DatabaseHandler;
import com.example.serkan.myapplication.Drawables.Score;
import com.example.serkan.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 19.03.2015.
 */
public class RankingActivity extends ListActivity {

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // hol daten aus der datenbank
        List<Score> scores = db.getAllScores();
        List<String> newList = new ArrayList<String>();

        for (Score cn : scores) {
            int id = cn.getScoreId();
            String date = cn.getScoreDate();
            int num = cn.getScoreNum();
            // speichere die daten in eine String List
            newList.add((date + " - " + num));
        }

        // zeige die daten in der ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newList);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        //db.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        db.close();
        super.onPause();
    }
}
