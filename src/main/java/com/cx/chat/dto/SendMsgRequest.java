package com.cx.chat.dto;

import java.io.Serializable;

/**
 * <>
 *
 * @Author: chenxin
 * @Date: 2021/3/12
 */
public class SendMsgRequest implements Serializable {

    private String token;

    private String msg;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
