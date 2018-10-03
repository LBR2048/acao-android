package com.penseapp.acaocontabilidade.chat.messages.model;

/**
 * Created by unity on 21/11/16.
 */

public class Message {

    private String text;
    private String senderId = "senderId";
    private String senderEmail = "senderEmail";
    private String key;
    private long timestamp = 0L;
    private String photoURL;
    private String photoDownloadURL;
    private String PDF;
    private String PDFDownloadURL;

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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotoDownloadURL() {
        return photoDownloadURL;
    }

    public void setPhotoDownloadURL(String photoDownloadURL) {
        this.photoDownloadURL = photoDownloadURL;
    }

    public String getPDF() {
        return PDF;
    }

    public void setPDF(String PDF) {
        this.PDF = PDF;
    }

    public String getPDFDownloadURL() {
        return PDFDownloadURL;
    }

    public void setPDFDownloadURL(String PDFDownloadURL) {
        this.PDFDownloadURL = PDFDownloadURL;
    }


}
