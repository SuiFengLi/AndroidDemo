package com.androidy.azsecuer.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by ljh on 2016/8/18.
 */
public class TaskInfo {
    private boolean isUserTask;
    private String packageName;
    private Drawable icon; // 图标
    private String taskName;
    private long memory;
    private boolean isSelected;
    public TaskInfo() {
        isUserTask =false;
        isSelected =false;
    }

    public boolean isUserTask() {
        return isUserTask;
    }

    public void setUserTask(boolean userTask) {
        isUserTask = userTask;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}
