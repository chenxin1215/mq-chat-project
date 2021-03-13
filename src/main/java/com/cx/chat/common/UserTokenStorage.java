package com.cx.chat.common;

import com.cx.chat.entity.ChatUser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <>
 *
 * @Author: chenxin
 * @Date: 2021/3/12
 */
public final class UserTokenStorage {

    private UserTokenStorage() {
    }

    // 保证单例
    private static class ChatUserMapClass {
        private static Map<String, ChatUser> chatUserMap;

        static {
            // 初始化
            chatUserMap = new HashMap<>();
        }
    }

    // 获取用户信息
    public static Map<String, ChatUser> getChatUserMap() {
        return ChatUserMapClass.chatUserMap;
    }

    public static String getToken() {
        UUID timeBasedUuid = org.apache.logging.log4j.core.util.UuidUtil.getTimeBasedUuid();
        return timeBasedUuid.toString().replace("-", "").toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(UserTokenStorage.getToken());
    }

}
