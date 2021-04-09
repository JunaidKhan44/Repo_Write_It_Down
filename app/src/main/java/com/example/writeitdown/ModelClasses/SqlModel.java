package com.example.writeitdown.ModelClasses;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqlModel extends SQLiteOpenHelper {

    private static final String DataBaseName = "WriteItDown.db";
    public static int TotalNumber;

    public SqlModel(Context context) {
        super(context, DataBaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Categories (id integer primary key autoincrement, categoryName char(400))");
        db.execSQL("CREATE TABLE Tasks (id integer primary key autoincrement, taskName char(400) ,CategoryID integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean Categories(String categoryName) {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            String query = "insert into Categories (categoryName) values ('" + categoryName + "')";
            sqLiteDatabase.execSQL(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    boolean SaveProfile() {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            String query = "CREATE TABLE Profile(id integer primary key autoincrement,UserName text ,Duration integer)";
            sqLiteDatabase.execSQL(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    boolean SaveProfileData(String Username, int delayTime) {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            String query = "insert into Profile (UserName,Duration) values ('" + Username + "','" + delayTime + "')";
            sqLiteDatabase.execSQL(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //data fatch karna k  liye yeh code ha
   public Cursor ShwoCategry() {
        Cursor csr;
        String query = "SELECT * FROM Categories";
        SQLiteDatabase sdb = getWritableDatabase();
        csr = sdb.rawQuery(query, null);
        return csr;
    }





    boolean delete() {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            String query = "drop Table History";
            sqLiteDatabase.execSQL(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }





}