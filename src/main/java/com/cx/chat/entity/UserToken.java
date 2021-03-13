package com.cx.chat.entity;

import java.io.Serializable;

/**
 * <>
 *
 * @Author: chenxin
 * @Date: 2021/3/12
 */
public class UserToken implements Serializable {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
