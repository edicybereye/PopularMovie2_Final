package com.edikurniawan.popmovie.application;


import android.app.Application;

import com.edikurniawan.popmovie.retrofit.MovieAPI;


public class App extends Application {
    private static MovieAPI.MovieClient movieClient;

    @Override
    public void onCreate() {
        super.onCreate();
//        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().build();
//        Realm.setDefaultConfiguration(config);
        movieClient = new MovieAPI.MovieClient();
    }

    public static MovieAPI.MovieClient getMovieClient() {
        return movieClient;
    }
}
