package com.edikurniawan.popmovie.db.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.edikurniawan.popmovie.BuildConfig;
import com.edikurniawan.popmovie.db.DataBaseHelper;
import com.edikurniawan.popmovie.db.ManageMovieTbl;

/**
 * Created by Rezky Aulia Pratama on 7/29/2017.
 */

public class ContentProviderHelper extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    public static final int CODE_GENRE = 200;
    public static final int CODE_GENRE_WITH_ID = 201;

    public static final int CODE_MOVIE_GENRE = 300;
    public static final int CODE_MOVIE_GENRE_WITH_ID = 301;

    public static final int CODE_FAVORITE = 400;
    public static final int CODE_FAVORITE_WITH_ID = 401;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DataBaseHelper mDbHelper;

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for Sunshine.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "MOVIE";


    /* The base CONTENT_URI used to query the Weather table from the content provider */
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .build();


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = DataBaseHelper.init(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case CODE_MOVIE:
                retCursor =  db.query(ManageMovieTbl.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_MOVIE_WITH_ID :
                String id = uri.getPathSegments().get(1);

                retCursor =  db.query(ManageMovieTbl.TABLE_NAME,
                        projection,
                        ManageMovieTbl.ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsInserted = 0;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE:
                db.beginTransaction();
                rowsInserted = 0;

                try {
                    for (ContentValues value : values) {
                        long movieId =
                                value.getAsLong(ManageMovieTbl.ID);

                        long _id = db.insertWithOnConflict(ManageMovieTbl.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;


            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        long id = 0;

        switch (match) {
            case CODE_MOVIE:
                id = db.insert(ManageMovieTbl.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted; // starts as 0

        switch (match) {
            case CODE_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(ManageMovieTbl.TABLE_NAME,
                        ManageMovieTbl.ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
