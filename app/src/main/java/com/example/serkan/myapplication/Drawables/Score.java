package com.example.serkan.myapplication.Drawables;

/**
 * Created on 22.04.2015.
 */
public class Score{
    private String scoreDate;
    private int scoreNum;
    private int scoreId;

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
    public String getScoreDate(){
        return scoreDate;
    }

    //set Date
    public void setScoreDate(String date){
        this.scoreDate = date;
    }

    //get Score
    public int getScoreNum(){
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
