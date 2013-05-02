package com.tibo.TwitterReader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class MainActivity extends Activity {

    private String CALLBACK_URL = "callback://tweeter";
    private Twitter twitter;
    private Button login;

    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            System.setProperty("http.keepAlive", "false");

            //check for saved log in details..
            checkForSavedLogin();

            //set consumer and provider on teh Application service
            getConsumerProvider();

            //Define login button and listener
            login = (Button)findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    askOAuth();
                }
            });
        }

    private void checkForSavedLogin() {
     // Get Access Token and persist it
     AccessToken a = getAccessToken();
     if (a==null) return; //if there are no credentials stored then return to usual activity

     // initialize Twitter4J
     twitter = new TwitterFactory().getInstance();
     twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
     twitter.setOAuthAccessToken(a);
     ((TwitterApplication)getApplication()).setTwitter(twitter);

     startFirstActivity();
     finish();
    }

    private void askOAuth() {
     try {
      consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
      provider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token", "http://twitter.com/oauth/authorize");
      String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
      Toast.makeText(this, "Please authorize this app!", Toast.LENGTH_LONG).show();
      setConsumerProvider();
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
     } catch (Exception e) {
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
     }
    }


    @Override
    protected void onResume() {
     super.onResume();
     if (this.getIntent()!=null && this.getIntent().getData()!=null){
      Uri uri = this.getIntent().getData();
      if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
       String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
       try {
        // this will populate token and token_secret in consumer
        provider.retrieveAccessToken(consumer, verifier);

        // Get Access Token and persist it
        AccessToken a = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
        storeAccessToken(a);

        // initialize Twitter4J
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        twitter.setOAuthAccessToken(a);
        ((TwitterApplication)getApplication()).setTwitter(twitter);
        //Log.e("Login", "Twitter Initialised");

        startFirstActivity();

       } catch (Exception e) {
        //Log.e(APP, e.getMessage());
        e.printStackTrace();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
       }
      }
     }
    }
}
