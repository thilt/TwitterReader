package com.tibo.TwitterReader.model;

import android.app.Application;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.Twitter;

/**
 * Created with IntelliJ IDEA.
 * User: tibo
 * Date: 04/06/13
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public class TwitterApplication extends Application {


    private Twitter twitter;
    private OAuthProvider provider;
    private CommonsHttpOAuthConsumer consumer;

    /**
     * @return the twitter
     */
    public Twitter getTwitter() {
        return twitter;
    }

    /**
     * @param twitter the twitter to set
     */
    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }


    /**
     * @param provider the provider to set
     */
    public void setProvider(OAuthProvider provider) {
        this.provider = provider;
    }

    /**
     * @return the provider
     */
    public OAuthProvider getProvider() {
        return provider;
    }

    /**
     * @param consumer the consumer to set
     */
    public void setConsumer(CommonsHttpOAuthConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * @return the consumer
     */
    public CommonsHttpOAuthConsumer getConsumer() {
        return consumer;
    }
}
