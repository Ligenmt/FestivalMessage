package com.ligenmt.festivalmessage.bean;

/**
 * Created by lenov0 on 2015/10/6.
 */
public class Message {

    private int id;
    private int festivalId;
    private String content;

    public Message(int id, int festivalId, String content) {
        this.id = id;
        this.festivalId = festivalId;
        this.content = content;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(int festivalId) {
        this.festivalId = festivalId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
