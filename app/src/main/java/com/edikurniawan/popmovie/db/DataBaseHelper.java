package com.edikurniawan.popmovie.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rezky Aulia Pratama on 8/8/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper{
    private static DataBaseHelper ourInstance;

    public static final String DATABASE_NAME = "PopularMovie.db";
    public static final int DATABASE_VERSION = 1;

    private static String PASSWORD;

    private SQLiteDatabase database;
    private Context mContext;


    public static DataBaseHelper init(Context context) {
        ourInstance = new DataBaseHelper(context, DATABASE_VERSION);
        return ourInstance;
    }


    public DataBaseHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);

        mContext = context;
        database = getWritableDatabase();


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("Databasehelper","SQL : ONCREATE");
        final String CREATE_TABLE =
                "CREATE TABLE " + ManageMovieTbl.TABLE_NAME + " (" +
                        ManageMovieTbl.ID                 + " INTEGER PRIMARY KEY, "  +
                        ManageMovieTbl.POSTER_PATH        + " STRING NOT NULL, "      +
                        ManageMovieTbl.OVERVIEW           + " STRING NOT NULL, "      +
                        ManageMovieTbl.RELEASE_DATE       + " STRING NOT NULL, "      +
                        ManageMovieTbl.ORIGINAL_TITLE     + " STRING NOT NULL, "      +
                        ManageMovieTbl.BACKDROP_PATH      + " STRING NOT NULL, "      +
                        ManageMovieTbl.VOTE_AVERAGE       + " REAL NOT NULL, "        +
                        " UNIQUE (" + ManageMovieTbl.ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + ManageMovieTbl.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);

    }
}
