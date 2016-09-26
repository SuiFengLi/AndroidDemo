package com.androidy.azsecuer.entity;

/**
 * Created by ljh on 2016/8/11.
 */
public class CommunicationInfo {
    private String name;
    private int idx;

    public CommunicationInfo(String name, int idx) {
        this.name = name;
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    @Override
    public String toString() {
        return "CommunicationInfo{" +
                "name='" + name + '\'' +
                ", idx=" + idx +
                '}';
    }
}
