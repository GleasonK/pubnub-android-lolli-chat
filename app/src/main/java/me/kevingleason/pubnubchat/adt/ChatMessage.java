package me.kevingleason.pubnubchat.adt;

/**
 * Created by GleasonK on 7/11/15.
 *
 * ChatMessage is used to hold information that is transmitted using PubNub.
 * A message in this app has a username, message, and timestamp.
 */
public class ChatMessage {
    private String username;
    private String message;
    private long timeStamp;

    public ChatMessage(String username, String message, long timeStamp){
        this.username  = username;
        this.message   = message;
        this.timeStamp = timeStamp;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
