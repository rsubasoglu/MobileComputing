package com.example.serkan.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.serkan.myapplication.Drawables.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 22.04.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "HighScore";

    // Score table name
    private static final String TABLE_SCORES = "highscores";

    // Score Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_SCORE_NUMBER = "score_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " DATE,"
                + KEY_SCORE_NUMBER + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Adding new score
    public void addScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, score.getScoreDate()); // Score Date
        values.put(KEY_SCORE_NUMBER, score.getScoreNum()); // Score Number

        // Inserting Row
        db.insert(TABLE_SCORES, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Scores
    public List<Score> getAllScores() {
        List<Score> scoreList = new ArrayList<Score>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setScoreId(Integer.parseInt(cursor.getString(0)));
                score.setScoreDate(cursor.getString(1));
                int num = Integer.parseInt(cursor.getString(2));
                score.setScoreNum(Integer.parseInt(cursor.getString(2)));
                // Adding contact to list
                scoreList.add(score);
            } while (cursor.moveToNext());
        }

        // return contact list
        return scoreList;
    }
}
