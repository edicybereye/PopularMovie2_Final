package com.edikurniawan.popmovie.retrofit;

import com.edikurniawan.popmovie.BuildConfig;
import com.edikurniawan.popmovie.models.MovieModel;
import com.edikurniawan.popmovie.models.ReviewModel;
import com.edikurniawan.popmovie.models.TrailerModel;

import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieAPI {


    @GET("/3/movie/{sort}")
    Call<Movies> loadMovies(@Path("sort") String sort, @Query("api_key") String api_key);

    @GET("/3/movie/{id}/videos")
    Call<Trailers> loadTrailers(@Path("id") String id, @Query("api_key") String api_key);

    @GET("/3/movie/{id}/reviews")
    Call<Reviews> loadReviews(@Path("id") String id, @Query("api_key") String api_key);


    class MovieClient
    {
        private MovieAPI movieAPI;


        public MovieClient()
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            movieAPI = retrofit.create(MovieAPI.class);
        }

        public MovieAPI getMovieAPI()
        {
            return movieAPI;
        }
    }

    class Movies {
        public List<MovieModel> results;
    }

    class Reviews {
        public List<ReviewModel> results;
    }

    class Trailers {
        public List<TrailerModel> results;
    }
}
