package com.edikurniawan.popmovie.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edikurniawan.popmovie.R;
import com.edikurniawan.popmovie.models.ReviewModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ReviewModel> data = new ArrayList<>();


    public ReviewAdapter(Context context, ArrayList<ReviewModel> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.review_list_item, parent, false);
        viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ((MyItemHolder) holder).authorText.setText(data.get(position).getAuthor());
        ((MyItemHolder) holder).reviewText.setText(data.get(position).getcontent());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyItemHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.authorText)
        TextView authorText;
        @Bind(R.id.reviewText)
        TextView reviewText;


        public MyItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }


}