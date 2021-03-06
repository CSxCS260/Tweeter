package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.Viewholder>{

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter (Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Tweet tweet = tweets.get(position);
        
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear(){
         tweets.clear();
         notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweetList){
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView profileImageIV;
        TextView tweetBodyTV;
        TextView screenNameTV;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            profileImageIV = itemView.findViewById(R.id.profileImageIV);
            tweetBodyTV = itemView.findViewById(R.id.tweetTV);
            screenNameTV = itemView.findViewById(R.id.usernameTV);
        }

        public void bind(Tweet tweet) {
            tweetBodyTV.setText(tweet.body);
            screenNameTV.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageUrl).into(profileImageIV);

        }
    }
}
