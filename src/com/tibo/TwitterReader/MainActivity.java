package com.tibo.TwitterReader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.tibo.TwitterReader.model.TwitterApplication;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class MainActivity extends Activity {

    private static final int ACTIVITY_LATEST_TWEETS = Menu.FIRST + 6;
    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";
    private static final String PREFS_NAME = "TwitterLogin";
    private static final String CALLBACK_URL = "callback://tweeter";
    private OAuthProvider provider;
    private CommonsHttpOAuthConsumer consumer;
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
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                askOAuth();
            }
        });
    }

    private void checkForSavedLogin() {
        // Get Access Token and persist it
        AccessToken a = getAccessToken();
        if (a == null) return; //if there are no credentials stored then return to usual activity

        // initialize Twitter4J
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        twitter.setOAuthAccessToken(a);
        ((TwitterApplication) getApplication()).setTwitter(twitter);

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
        if (this.getIntent() != null && this.getIntent().getData() != null) {
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
                    ((TwitterApplication) getApplication()).setTwitter(twitter);
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


    /**
     * Kick off the activity to display
     */
    private void startFirstActivity() {
        System.out.println("STARTING FIRST ACTIVITY!");
        Intent i = new Intent(this, TweetsActivity.class);
        startActivityForResult(i, ACTIVITY_LATEST_TWEETS);
    }

    /**
     * This method checks the shared prefs to see if we have persisted a user token/secret
     * if it has then it logs on using them, otherwise return null
     *
     * @return AccessToken from persisted prefs
     */
    private AccessToken getAccessToken() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = settings.getString("accessTokenToken", "");
        String tokenSecret = settings.getString("accessTokenSecret", "");
        if (token!=null && tokenSecret!=null && !"".equals(tokenSecret) && !"".equals(token)){
            return new AccessToken(token, tokenSecret);
        }
        return null;
    }

    /**
     * This method persists the Access Token information so that a user
     * is not required to re-login every time the app is used
     *
     * @param a - the access token
     */
    private void storeAccessToken(AccessToken a) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("accessTokenToken", a.getToken());
        editor.putString("accessTokenSecret", a.getTokenSecret());
        editor.commit();
    }


    /**
     * Get the consumer and provider from the application service (in the case that the
     * activity is restarted so the objects are not lost
     */
    private void getConsumerProvider() {
        OAuthProvider p = ((TwitterApplication)getApplication()).getProvider();
        if (p!=null){
            provider = p;
        }
        CommonsHttpOAuthConsumer c = ((TwitterApplication)getApplication()).getConsumer();
        if (c!=null){
            consumer = c;
        }
    }

    /**
     * Set the consumer and provider from the application service (in the case that the
     * activity is restarted so the objects are not lost)
     */
    private void setConsumerProvider() {
        if (provider!=null){
            ((TwitterApplication)getApplication()).setProvider(provider);
        }
        if (consumer!=null){
            ((TwitterApplication)getApplication()).setConsumer(consumer);
        }
    }
}
