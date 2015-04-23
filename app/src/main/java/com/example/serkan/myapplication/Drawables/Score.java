package com.example.serkan.myapplication.Drawables;

/**
 * Created by Ahmed on 22.04.2015.
 */
public class Score{
    private static String scoreDate;
    private static int scoreNum;
    private static int scoreId;

    //empty Konstruktor
    public Score(){

    }

    //Konstruktor
    public Score(int id, String date, int num){
        scoreId=id;
        scoreDate=date;
        scoreNum=num;
    }

    //Konstruktor
    public Score(String date, int num){
        scoreDate=date;
        scoreNum=num;
    }

    //get ID
    public int getScoreId(){
        return scoreId;
    }

    //set ID
    public void setScoreId(int id){
        this.scoreId = id;
    }

    //get Date
    public static String getScoreDate(){
        return scoreDate;
    }

    //set Date
    public void setScoreDate(String date){
        this.scoreDate = date;
    }

    //get Score
    public static int getScoreNum(){
        return scoreNum;
    }

    //set Score
    public void setScoreNum(int num){
        this.scoreNum = num;
    }

    //get Score in Text
    public String getScoreText()
    {
        return scoreId+" : "+scoreDate+" - "+scoreNum;
    }
}
