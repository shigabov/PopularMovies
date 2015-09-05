package com.example.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Артем on 05.09.2015.
 */

public class MovieDBOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MOVIEDB";
    public static final String MOVIES_TABLE_NAME = "movies";

    MovieDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MOVIES_TABLE_NAME + "(" +
                        "mv_id INTEGER PRIMARY KEY," +
                        "mv_title TEXT," +
                        "mv_release_date TEXT, " +
                        "mv_overview TEXT, " +
                        "mv_vote_average REAL," +
                        "mv_poster_path TEXT );"
        );
    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){

    }
}