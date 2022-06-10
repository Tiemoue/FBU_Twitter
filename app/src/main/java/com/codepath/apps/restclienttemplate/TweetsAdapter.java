package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet  tweet = tweets.get(position);

        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImg = itemView.findViewById(R.id.ivTweetImg);


        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            String format = tweet.user.name + " @" + tweet.user.screenName + " " + tweet.date.replace(" ", "");
            tvScreenName.setText(format);
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCorners(90)).into(ivProfileImage);
            if(tweet.imageURL != null){
                Glide.with(context).load(tweet.imageURL).override(500,350).transform(new RoundedCorners(20)).into(ivTweetImg);
                ivTweetImg.setVisibility(View.VISIBLE);
            }
            else{
                ivTweetImg.setVisibility(View.GONE);
            }

        }
        // Your holder should contain a member variable
        // for any view that will be set as you render a row


    }



}
