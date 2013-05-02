package com.tibo.TwitterReader.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tibo.TwitterReader.R;

import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tibo
 * Date: 16/03/13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
public class TweetItemAdapter extends ArrayAdapter<Tweet> {

    private List<Tweet> tweets;
    private int textViewResourceId;

    public TweetItemAdapter(Context context, int textViewResourceId, List<Tweet> tweets) {
        super(context, textViewResourceId, tweets);
        this.tweets = tweets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout tweetView;
        if (convertView == null) {
            tweetView = new LinearLayout(getContext());

            LayoutInflater layoutInflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(textViewResourceId, tweetView, true);
        } else {
            tweetView = (LinearLayout) convertView;
        }

        Tweet tweet = tweets.get(position);
        if (tweet != null) {
            TextView username = (TextView) tweetView.findViewById(R.id.username);
            TextView message = (TextView) tweetView.findViewById(R.id.message);
            ImageView image = (ImageView) tweetView.findViewById(R.id.avatar);

            if (username != null) {
                username.setText(tweet.username);
            }

            if (message != null) {
                message.setText(tweet.message);
            }

            if (image != null) {
                image.setImageBitmap(getBitmap(tweet.image_url));
            }
        }

        return tweetView;
    }

    public Bitmap getBitmap(String bitmapUrl) {
        try {
            URL url = new URL(bitmapUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception ex) {
            return null;
        }
    }
}
