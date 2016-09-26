package com.androidy.azsecuer.entity;

/**
 * Created by ljh on 2016/8/15.
 */
public class PhoneManagerInfoChild {
    private String title;
    private String text;

    public PhoneManagerInfoChild(String title, String text) {
        this.text = text;
        this.title = title;
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
}
