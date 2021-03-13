package com.cx.chat.controller;

import com.cx.chat.common.UserTokenStorage;
import com.cx.chat.dto.BaseRequest;
import com.cx.chat.entity.ChatUser;
import com.cx.chat.entity.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <>
 *
 * @Author: chenxin
 * @Date: 2021/3/11
 */
@RestController @RequestMapping("user") public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping("login") public UserToken login(BaseRequest request) {
        String nickName = request.getNickName();
        if (nickName == null || nickName.equals("")) {
            LOGGER.info("登陆名为空！");
            return null;
        }
        LOGGER.info("登陆 = " + nickName);

        String token = UserTokenStorage.getToken();
        Map<String, ChatUser> chatUserMap = UserTokenStorage.getChatUserMap();
        ChatUser chatUser = new ChatUser();
        chatUser.setNickName(nickName);
        chatUserMap.put(token, chatUser);

        UserToken userToken = new UserToken();
        userToken.setToken(token);
        return userToken;
    }

    @PostMapping("/getUserInfo") public ChatUser getUserInfo(BaseRequest request) {
        String token = request.getToken();
        if (token == null || token.equals("")) {
            LOGGER.info("token为空！");
            return null;
        }

        LOGGER.info("token = " + token);
        Map<String, ChatUser> chatUserMap = UserTokenStorage.getChatUserMap();
        ChatUser chatUser = chatUserMap.get(token);
        if (chatUser != null) {
            LOGGER.info("chatUser.getNickName() = " + chatUser.getNickName());
        }
        return chatUser;
    }

}
