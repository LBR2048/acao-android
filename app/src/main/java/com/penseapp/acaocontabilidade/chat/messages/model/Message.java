package com.penseapp.acaocontabilidade.chat.messages.model;

/**
 * Created by unity on 21/11/16.
 */

public class Message {

    private String text = "text";
    private String senderId = "senderId";
    private String senderEmail = "senderEmail";
    private String key;
    private long timestamp = 0L;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
