package com.edikurniawan.popmovie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.Uri;
import android.provider.BaseColumns;

import com.edikurniawan.popmovie.models.MovieModel;

import java.util.List;

/**
 * Created by Rezky Aulia Pratama on 8/8/2017.
 */

public class ManageMovieTbl implements BaseColumns {
    DataBaseHelper mDbHelper;

    public ManageMovieTbl(DataBaseHelper helper) {
        this.mDbHelper = helper;
    }

    public final static String TABLE_NAME = "MovieTbl";

    public final static String ID = "Id";
    public final static String POSTER_PATH = "PosterPath";
    public final static String ADULT = "Adult";
    public final static String OVERVIEW = "Overview";
    public final static String RELEASE_DATE = "ReleaseDate";
    public final static String ORIGINAL_TITLE = "OriginalTitle";
    public final static String ORIGINAL_LANGUAGE = "OriginalLanguage";
    public final static String TITLE = "Title";
    public final static String BACKDROP_PATH = "BackdropPath";
    public final static String POPULARITY = "Popularity";
    public final static String VOTE_COUNT = "VoteCount";
    public final static String VIDEO = "Video";
    public final static String VOTE_AVERAGE = "VoteAverage";


    public static ContentValues contentValue(MovieModel movie) {
        ContentValues value = new ContentValues();
        value.put(ID, movie.getId());
        value.put(OVERVIEW, movie.getOverview());
        value.put(RELEASE_DATE, movie.getrelease_date());
        value.put(ORIGINAL_TITLE, movie.getoriginal_title());
        value.put(POSTER_PATH, movie.getposter_path());
        value.put(VOTE_AVERAGE, movie.getvote_average());
        value.put(BACKDROP_PATH, movie.getBackdrop_path());
        return value;
    }


    public static ContentValues[] contentValues(List<MovieModel> movies) {

        ContentValues[] contentValues = new ContentValues[movies.size()];
        int i = 0;
        for (MovieModel movie : movies) {
            ContentValues value = new ContentValues();
            value.put(ID, movie.getId());
            value.put(POSTER_PATH, movie.getposter_path());
            value.put(OVERVIEW, movie.getOverview());
            value.put(RELEASE_DATE, movie.getrelease_date());
            value.put(ORIGINAL_TITLE, movie.getoriginal_title());
            value.put(BACKDROP_PATH, movie.getBackdrop_path());
            value.put(VOTE_AVERAGE, movie.getvote_average());
            contentValues[i] = value;
            i++;
        }

        return contentValues;
    }

    public static MovieModel assign(Cursor cursor) {

        MovieModel movie = new MovieModel();

        movie.setId(cursor.getString(cursor.getColumnIndex(ID)));
        movie.setPoster_path(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
        movie.setRelease_date(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
        movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
        movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(BACKDROP_PATH)));
        movie.setVote_average(cursor.getFloat(cursor.getColumnIndex(VOTE_AVERAGE)));

        return movie;


    }
}
