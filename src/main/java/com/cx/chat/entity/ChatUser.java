package com.cx.chat.entity;

import java.io.Serializable;

/**
 * @Author: chenxin
 * @Date: 2021/3/11
 */
public class ChatUser implements Serializable {

    private String nickName;

    private String userImg;

    private Integer sex;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
