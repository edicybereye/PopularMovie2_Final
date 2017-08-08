package com.edikurniawan.popmovie.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable{

    private String release_date;
    private String original_title;
    private String poster_path;
    private String overview;
    private float vote_average;
    private String backdrop_path;
    private String id;


    public MovieModel(){

    }

    public MovieModel(MovieModel movieModel){
        release_date = movieModel.release_date;
        original_title = movieModel.original_title;
        poster_path = movieModel.poster_path;
        overview = movieModel.overview;
        vote_average = movieModel.vote_average;
        backdrop_path = movieModel.backdrop_path;
        id = movieModel.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeFloat(vote_average);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(id);

    }

    protected MovieModel(Parcel in) {
        original_title = in.readString();
        poster_path = in.readString();
        vote_average = in.readFloat();
        backdrop_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getoriginal_title() {
        return original_title;
    }



    public String getposter_path() {
        return poster_path;
    }


    public String getOverview() {
        return overview;
    }



    public float getvote_average() {
        return vote_average;
    }



    public String getrelease_date() {
        return release_date;
    }


    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getId() {
        return id;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setId(String id) {
        this.id = id;
    }
}
