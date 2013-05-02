package com.tibo.TwitterReader.model;

/**
 * Created with IntelliJ IDEA.
 * User: tibo
 * Date: 16/03/13
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
public class Tweet {

    public String username;
    public String message;
    public String image_url;

    public Tweet(String username, String message, String image_url) {
        this.username = username;
        this.message = message;
        this.image_url = image_url;
    }
}
