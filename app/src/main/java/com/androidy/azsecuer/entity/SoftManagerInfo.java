package com.androidy.azsecuer.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by ljh on 2016/8/22.
 */
public class SoftManagerInfo {
    private String packageName;
    private Drawable icon; // 图标
    private String taskName;
    private String taskVersion;
    private boolean isSelected;
    private boolean isUserTask;
    public SoftManagerInfo() {
        isSelected =false;
        isUserTask =false;
    }

    public SoftManagerInfo(String packageName, Drawable icon, String taskName, String taskVersion) {
        this.packageName = packageName;
        this.icon = icon;
        this.taskName = taskName;
        this.taskVersion = taskVersion;
        isSelected =false;
        isUserTask =false;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskVersion() {
        return taskVersion;
    }

    public void setTaskVersion(String taskVersion) {
        this.taskVersion = taskVersion;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isUserTask() {
        return isUserTask;
    }

    public void setUserTask(boolean userTask) {
        isUserTask = userTask;
    }

    @Override
    public String toString() {
        return "SoftManagerInfo{" +
                "packageName='" + packageName + '\'' +
                ", icon=" + icon +
                ", taskName='" + taskName + '\'' +
                ", taskVersion='" + taskVersion + '\'' +
                ", isSelected=" + isSelected +
                ", isUserTask=" + isUserTask +
                '}';
    }
}
