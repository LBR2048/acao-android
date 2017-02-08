package com.penseapp.acaocontabilidade.chat.model;

public class Chat {
    private String key;
    private String name = "name";
    private int unreadMessageCount = 0;
    private long latestMessageTimestamp = 0;

    public Chat() {

    }

    public Chat(String name){
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public long getLatestMessageTimestamp() {
        return latestMessageTimestamp;
    }

    public void setLatestMessageTimestamp(long latestMessageTimestamp) {
        this.latestMessageTimestamp = latestMessageTimestamp;
    }

    @Override
    public String toString() {
        return getName();
    }
}