package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.AppCompatImageButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.util.List;

import okhttp3.Headers;

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
        ImageButton ibFav;
        TextView tvFavCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImg = itemView.findViewById(R.id.ivTweetImg);
            ibFav = itemView.findViewById(R.id.ibfav);
            tvFavCount = itemView.findViewById(R.id.tvFavCount);

        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            String format = tweet.user.name + " @" + tweet.user.screenName + " • " + tweet.date.replace(" ", "");
            tvScreenName.setText(format);

            tvFavCount.setText(String.valueOf(tweet.favCount));

            if(tweet.isFav){
                Drawable newImg = context.getDrawable(R.drawable.ic_vector_heart);
                ibFav.setImageDrawable(newImg);

            }else{
                Drawable newImg = context.getDrawable(R.drawable.ic_vector_heart_stroke);
                ibFav.setImageDrawable(newImg);
            }


            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCorners(90)).into(ivProfileImage);
            if(tweet.imageURL != null){
                Glide.with(context).load(tweet.imageURL).override(500,350).transform(new RoundedCorners(20)).into(ivTweetImg);
                ivTweetImg.setVisibility(View.VISIBLE);
            }
            else{
                ivTweetImg.setVisibility(View.GONE);
            }
           ibFav.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(!(tweet.isFav)){
                       Drawable newImg = context.getDrawable(R.drawable.ic_vector_heart);
                       ibFav.setImageDrawable(newImg);
                       tweet.isFav = true;
                       tweet.favCount ++;
                       tvFavCount.setText(String.valueOf(tweet.favCount));
                       TwitterApp.getRestClient(context).favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                           @Override
                           public void onSuccess(int statusCode, Headers headers, JSON json) {
                               Log.i("adapter", "this tweet has been fav");
                           }

                           @Override
                           public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                               Log.e("adapter", "this failed");
                           }
                       });
                   }else{
                       Drawable newImg = context.getDrawable(R.drawable.ic_vector_heart_stroke);
                       ibFav.setImageDrawable(newImg);
                       tweet.isFav = false;
                       tweet.favCount --;
                       tvFavCount.setText(String.valueOf(tweet.favCount));
                       TwitterApp.getRestClient(context).unFavoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                           @Override
                           public void onSuccess(int statusCode, Headers headers, JSON json) {
                               Log.i("adapter", "this has been unfav");
                           }

                           @Override
                           public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                           }
                       });
                   }
                   // change the drawable icon
                   // increment the count
               }
           });
        }
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

    }



}
