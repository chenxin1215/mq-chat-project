package com.cx.chat.controller;

import com.cx.chat.common.UserTokenStorage;
import com.cx.chat.dto.SendMsgRequest;
import com.cx.chat.entity.ChatMessage;
import com.cx.chat.entity.ChatUser;
import com.cx.chat.service.ReceiveMsgService;
import com.cx.chat.service.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <消息控制器>
 *
 * @Author: chenxin
 * @Date: 2021/3/11
 */
@RestController @RequestMapping("/msg") public class MsgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgController.class);

    @Autowired private SendService sendService;

    @Autowired private ReceiveMsgService receiveMsgService;

    @PostMapping("/sendMsg") public Boolean sendMsg(SendMsgRequest request) {
        String token = request.getToken();
        if (token == null || token.equals("")) {
            LOGGER.info("token 不能为空");
            return false;
        }

        ChatUser chatUser = UserTokenStorage.getChatUserMap().get(token);
        if (chatUser == null) {
            LOGGER.info("token错误 找不到用户");
            return false;
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSendUserName(chatUser.getNickName());
        chatMessage.setMsg(request.getMsg());
        sendService.sendMeg(chatMessage);

        return true;
    }

    @PostMapping("/getAllList") public List<ChatMessage> getAllList() {
        return receiveMsgService.getMsgList();
    }

}
