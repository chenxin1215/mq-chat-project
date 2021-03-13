package com.cx.chat.common;

import com.cx.chat.entity.ChatUser;

public final class UserStorage {

    private UserStorage() {
    }

    private static final ThreadLocal<ChatUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 赋值ThreadLocal
     */
    public static void setUser(ChatUser user) {
        USER_THREAD_LOCAL.set(user);
    }

    /**
     * 获取ThreadLocal
     */
    public static ChatUser getUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 清空
     */
    public static void clean() {
        USER_THREAD_LOCAL.remove();
    }
}
