package com.penseapp.acaocontabilidade.news.model;

/**
 * Created by unity on 09/01/17.
 */

public class News {

    private String date = "date";
    private String text = "text";
    private String title = "title";
    private String key;

    public News() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
