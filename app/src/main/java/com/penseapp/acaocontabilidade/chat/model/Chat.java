package com.penseapp.acaocontabilidade.chat.model;

public class Chat {
    private String key;
    private String name = "name";

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

    @Override
    public String toString() {
        return getName();
    }
}