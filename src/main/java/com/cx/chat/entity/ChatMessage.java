package com.cx.chat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <聊天消息>
 *
 * @Author: chenxin
 * @Date: 2021/3/11
 */
public class ChatMessage implements Serializable {

    /**
     * 消息发送人
     */
    private String sendUserName;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override public String toString() {
        return "ChatMessage{" + "sendUserName='" + sendUserName + '\'' + ", msg='" + msg + '\'' + ", sendTime="
                + sendTime + '}';
    }
}
