package com.asmaa.googlecalendertask.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_EVENTS_TABLE="create table "+DBStructure.EVENT_TABLE_NAME+ "( ID INTEGER PRIMARY KEY AUTOINCREMENT ,"
      +DBStructure.EVENT +" TEXT , "+DBStructure.TIME +" TEXT , "+DBStructure.DATE +"TEXT ,"+DBStructure.MONTH +"TEXT , " +
            DBStructure.YEAR +" TEXT ) ";

    private static final String DROP_EVENT_TABLE = "DROP TABLE IF EXISTS" + DBStructure.EVENT_TABLE_NAME;


    public DataBaseHelper(@Nullable Context context ) {
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_EVENTS_TABLE);
        onCreate(db);

    }


    public void SaveEvents(String events ,String time , String date , String month , String year , SQLiteDatabase database){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT,events);
        contentValues.put(DBStructure.TIME,time);
        contentValues.put(DBStructure.DATE,date);
        contentValues.put(DBStructure.MONTH,month);
        contentValues.put(DBStructure.YEAR,year);
        database.insert(DBStructure.EVENT_TABLE_NAME,null,contentValues);
    }

    public Cursor ReadEvents(String date , SQLiteDatabase database){

        String [] Projections = {DBStructure.EVENT,DBStructure.TIME ,DBStructure.DATE,DBStructure.MONTH ,DBStructure.YEAR};
        String Selection = DBStructure.DATE+"=?";
        String [] SelectionArgs = {date};

        return database.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);


    }

    public Cursor ReadEventsMonth(String month , String year , SQLiteDatabase database){

        String [] Projections = {DBStructure.EVENT,DBStructure.TIME ,DBStructure.DATE,DBStructure.MONTH ,DBStructure.YEAR};
        String Selection = DBStructure.MONTH+"=? and "+DBStructure.YEAR;
        String [] SelectionArgs = {month,year};


        //Problem looks like here
        // i know but i donot know why
        return database.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);

    }

}
