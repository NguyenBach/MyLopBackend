/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mylop.bean;

/**
 *
 * @author quangbach
 */
public class OtherInfomation {
    private int id;
    private String userId;
    private String infomation;
    private String value;

    public OtherInfomation() {
    }

    public OtherInfomation(String userId, String infomation, String value) {
        this.userId = userId;
        this.infomation = infomation;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
    
}
