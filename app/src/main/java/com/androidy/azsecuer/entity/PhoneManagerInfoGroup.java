package com.androidy.azsecuer.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by ljh on 2016/8/15.
 */
public class PhoneManagerInfoGroup {
        private Drawable icon;
        private String text;

    public PhoneManagerInfoGroup(Drawable icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
