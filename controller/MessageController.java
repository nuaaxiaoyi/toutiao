package com.nowcoder.toutiao.controller;

import com.nowcoder.toutiao.aspect.LogAspect;
import com.nowcoder.toutiao.dao.MessageDAO;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.model.ViewObject;
import com.nowcoder.toutiao.service.MessageService;
import com.nowcoder.toutiao.service.UserService;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import org.apache.catalina.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by xiaoyy on 11/9/17.
 */
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})

    public String conversationList(Model model) {
        int localUserId = hostHolder.getUser().getId();
        try {



            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 5);
            model.addAttribute("localuser", localUserId);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = (msg.getFromId() == localUserId) ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("targetId", targetId);
                vo.set("totalCount", msg.getId());
                vo.set("unreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));

                conversations.add(vo);

            }
            model.addAttribute("number",String.valueOf(conversations.size()));

            model.addAttribute("conversations", conversations);

            return "letter";
        } catch (Exception e) {

            logger.error("获取站内信列表失败" + e.getMessage());
            logger.info("获取站内信列表失败");
        }
        return "letter";

    }


    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<ViewObject> messages = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            return "letterDetail";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {

        try {
            Message message = new Message();
            message.setContent(content);
            message.setToId(toId);
            message.setFromId(fromId);
            message.setCreatedDate(new Date());
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(0, String.valueOf(message.getId()));
        }catch (Exception e) {
            logger.error("添加消息失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "添加消息失败");
        }

    }
}
