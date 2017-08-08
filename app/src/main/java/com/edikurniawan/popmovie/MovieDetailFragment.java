package com.edikurniawan.popmovie;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.edikurniawan.popmovie.db.DataBaseHelper;
import com.edikurniawan.popmovie.db.ManageMovieTbl;
import com.edikurniawan.popmovie.db.content_provider.ContentProviderHelper;
import com.squareup.picasso.Picasso;
import com.edikurniawan.popmovie.adapters.ReviewAdapter;
import com.edikurniawan.popmovie.adapters.TrailerAdapter;
import com.edikurniawan.popmovie.application.App;
import com.edikurniawan.popmovie.models.MovieModel;
import com.edikurniawan.popmovie.models.ReviewModel;
import com.edikurniawan.popmovie.models.TrailerModel;
import com.edikurniawan.popmovie.retrofit.MovieAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MovieDetailFragment extends Fragment {


    public MovieDetailFragment() {
    }

    MovieModel movieModel;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.titleView)
    TextView titleView;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.releaseText)
    TextView releaseText;
    @Bind(R.id.trailersRecyclerView)
    RecyclerView trailersRecyclerView;
    @Bind(R.id.reviewsRecyclerView)
    RecyclerView reviewsRecyclerView;
    @Bind(R.id.noReviewView)
    TextView noReviewView;
    @Bind(R.id.noTrailerView)
    TextView noTrailerView;
    @Bind(R.id.extras)
    LinearLayout extraLayout;
    ArrayList<TrailerModel> trailerList;
    ArrayList<ReviewModel> reviewList;
    ReviewAdapter reviewAdapter;
    TrailerAdapter trailerAdapter;

    MenuItem item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments().containsKey("movie")) {

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
            if (appBarLayout != null) {
                appBarLayout.setTitle("");
           }
            movieModel = getArguments().getParcelable("movie");
            assert movieModel != null;
        }
        trailerList = new ArrayList<>();
        reviewList = new ArrayList<>();
        (new FetchReviews()).execute(movieModel.getId());
        (new FetchTrailers()).execute(movieModel.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this,rootView);

        titleView.setText(movieModel.getoriginal_title());

        Picasso.with(getActivity()).load(BuildConfig.IMAGE_URL+"/w342" + movieModel.getposter_path() + "?api_key?=" + BuildConfig.API_KEY).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);

        rating.setText(Float.toString(movieModel.getvote_average()).concat("/10"));
        ratingBar.setMax(5);
        ratingBar.setRating(movieModel.getvote_average() / 2f);

        overview.setText(movieModel.getOverview());
        releaseText.setText("Release Date: ".concat(movieModel.getrelease_date()));

        if (!isNetworkAvailable())
            extraLayout.setVisibility(View.INVISIBLE);

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext());

        trailersRecyclerView.setLayoutManager(trailerLayoutManager);
        reviewsRecyclerView.setLayoutManager(reviewLayoutManager);

        reviewAdapter = new ReviewAdapter(getContext(),reviewList);
        trailerAdapter = new TrailerAdapter(getContext(),trailerList);

        trailersRecyclerView.setAdapter(trailerAdapter);
        reviewsRecyclerView.setAdapter(reviewAdapter);

        trailersRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = "https://www.youtube.com/watch?v=".concat(trailerList.get(position).getKey());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

        }));

        reviewsRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(reviewList.get(position).getUrl()));
                startActivity(i);
            }
        }));
        return rootView;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.share).setVisible(true);
        item = menu.findItem(R.id.fav);
        item.setVisible(true);
        item.setIcon(!isFavourite() ? R.drawable.fav_remove : R.drawable.fav_add);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, movieModel.getoriginal_title());
                share.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=".concat(trailerList.get(0).getKey()));
                startActivity(Intent.createChooser(share, "Share Trailer!"));
                break;


            case R.id.fav:

                if (!isFavourite()) {
                    ContentValues value = ManageMovieTbl.contentValue(movieModel);

                    try{
                        getContext().getContentResolver().insert(ContentProviderHelper.CONTENT_URI, value);
                    }catch (SQLException e){

                    }

                } else {
                    String stringId = movieModel.getId();
                    Uri uri = ContentProviderHelper.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();

                    getContext().getContentResolver().delete(uri, null, null);
                }
                item.setIcon(!isFavourite() ? R.drawable.fav_remove : R.drawable.fav_add);

                break;


        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isFavourite(){
        String stringId = movieModel.getId();
        Uri uri = ContentProviderHelper.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Cursor cursor = getContext().getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);
        if (cursor != null){
            return cursor.getCount() > 0;
        }
        return false;
    }

    private class FetchReviews extends AsyncTask<String, Void,
            List<ReviewModel>> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<ReviewModel> doInBackground(String... params) {
            final String sort = params[0];
            App.getMovieClient().getMovieAPI().loadReviews(sort, BuildConfig.API_KEY).enqueue(new Callback<MovieAPI.Reviews>() {

                @Override
                public void onResponse(Response<MovieAPI.Reviews> response, Retrofit retrofit) {

                    for (int i = 0; i < response.body().results.size(); i++) {
                        reviewList.add(response.body().results.get(i));
                    }
                    reviewAdapter.notifyDataSetChanged();
                    if (reviewList.isEmpty()) {
                        reviewsRecyclerView.setVisibility(View.INVISIBLE);
                        noReviewView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(List<ReviewModel> movieModels) {
            super.onPostExecute(movieModels);
        }
    }

    private class FetchTrailers extends AsyncTask<String, Void,
            List<TrailerModel>> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<TrailerModel> doInBackground(String... params) {
            final String sort = params[0];
            App.getMovieClient().getMovieAPI().loadTrailers(sort, BuildConfig.API_KEY).enqueue(new Callback<MovieAPI.Trailers>() {

                @Override
                public void onResponse(Response<MovieAPI.Trailers> response, Retrofit retrofit) {

                    for (int i = 0; i < response.body().results.size(); i++) {
                        trailerList.add(response.body().results.get(i));
                    }
                    trailerAdapter.notifyDataSetChanged();
                    if (trailerList.isEmpty()) {
                        trailersRecyclerView.setVisibility(View.INVISIBLE);
                        noTrailerView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(List<TrailerModel> movieModels) {
            super.onPostExecute(movieModels);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
