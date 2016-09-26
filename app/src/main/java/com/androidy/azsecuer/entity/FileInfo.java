package com.androidy.azsecuer.entity;

import java.io.File;

/**
 * Created by ljh on 2016/8/25.
 */
public class FileInfo {
    private File file; // 文件对象
    private int romType;// 文件所在空间类型(内置0?外置1?)
    private String fileType; // 文件类型(图像?音频?)
    private String iconName;// 此文件所用图像名称
    private String openType;// 此文件的打开类型
    private boolean isSelect;

    public FileInfo(File file, int romType, String fileType, String iconName, String openType) {
        this.file = file;
        this.romType = romType;
        this.fileType = fileType;
        this.iconName = iconName;
        this.openType = openType;
        this.isSelect = false;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getRomType() {
        return romType;
    }

    public void setRomType(int romType) {
        this.romType = romType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
