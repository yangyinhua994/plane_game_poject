package com.example.plane.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.plane.dto.User;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<User> userList;

    public RankingAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RankingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ranking_view, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingAdapter.MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.ranking_textview1.setText(String.valueOf(user.getRanking()));
        holder.ranking_textview2.setText(user.getNumber());
        holder.ranking_textview3.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView ranking_textview1;
        private final TextView ranking_textview2;
        private final TextView ranking_textview3;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ranking_textview1 = itemView.findViewById(R.id.ranking_textview1);
            ranking_textview2 = itemView.findViewById(R.id.ranking_textview2);
            ranking_textview3 = itemView.findViewById(R.id.ranking_textview3);
        }
    }
}
