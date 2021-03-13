package com.cx.chat.dto;

import java.io.Serializable;

/**
 * <>
 *
 * @Author: chenxin
 * @Date: 2021/3/12
 */
public class BaseRequest implements Serializable {

    private String token;

    private String nickName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
