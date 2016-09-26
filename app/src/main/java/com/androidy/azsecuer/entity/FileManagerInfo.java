package com.androidy.azsecuer.entity;

/**
 * Created by ljh on 2016/8/23.
 */
public class FileManagerInfo {
    private String typeName;
    private String fileType;
    private long size;
    private boolean loading;

    public FileManagerInfo(String typeName, String fileType) {
        this.typeName = typeName;
        this.fileType = fileType;
        this.size=0;
        this.loading = false;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
