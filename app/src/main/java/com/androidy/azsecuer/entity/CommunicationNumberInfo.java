package com.androidy.azsecuer.entity;

/**
 * Created by ljh on 2016/8/11.
 */
public class CommunicationNumberInfo {
    private String name;
    private String number;

    public CommunicationNumberInfo(String name,String number){
        this.name = name;
        this.number = number;
    }

    public void setName(String name){
        this.name =name;
    }
    public String getName(){
        return name;
    }
    public void setNumber(String number){
        this.number = number;

    }
    public String getNumber(){
        return number;
    }

    @Override
    public String toString() {
        return "CommunicationNumberInfo{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
