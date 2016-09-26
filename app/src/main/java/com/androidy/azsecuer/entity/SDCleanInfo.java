package com.androidy.azsecuer.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by ljh on 2016/8/17.
 */
public class SDCleanInfo {
    private String softChinesename;
    private String apkname;
    private String filepath;
    private Drawable drawable;
    private long fileSize;
    private boolean isSelected;
    private boolean isCompleteFileSizeLoad;

    public SDCleanInfo(String softChinesename, String apkname, String filepath) {
        this.softChinesename = softChinesename;
        this.apkname = apkname;
        this.filepath = filepath;
    }

    public SDCleanInfo(String softChinesename, String apkname, String filepath, Drawable drawable, long fileSize) {
        this.softChinesename = softChinesename;
        this.apkname = apkname;
        this.filepath = filepath;
        this.drawable = drawable;
        this.fileSize = fileSize;
        isSelected = false;
    }

    public String getSoftChinesename() {
        return softChinesename;
    }

    public void setSoftChinesename(String softChinesename) {
        this.softChinesename = softChinesename;
    }

    public String getApkname() {
        return apkname;
    }

    public void setApkname(String apkname) {
        this.apkname = apkname;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isCompleteFileSizeLoad() {
        return isCompleteFileSizeLoad;
    }

    public void setCompleteFileSizeLoad(boolean completeFileSizeLoad) {
        isCompleteFileSizeLoad = completeFileSizeLoad;
    }

    @Override
    public String toString() {
        return "SDCleanInfo{" +
                "softChinesename='" + softChinesename + '\'' +
                ", apkname='" + apkname + '\'' +
                ", filepath='" + filepath + '\'' +
                ", drawable=" + drawable +
                ", fileSize=" + fileSize +
                ", isSelected=" + isSelected +
                ", isCompleteFileSizeLoad=" + isCompleteFileSizeLoad +
                '}';
    }
}
