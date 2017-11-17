package com.nowcoder.toutiao.async.handler;

import com.nowcoder.toutiao.async.EventHandler;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.service.MessageService;
import com.nowcoder.toutiao.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by xiaoyy on 11/11/17.
 */
@Component
public class LoginExceptionHandler implements EventHandler{
//一有异常登录，发一封站内信

    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;


    @Override
    public void doHandle(EventModel model) {
        //判断是否有异常登陆，比较复杂，不写了


        //然后发个信息
        Message message = new Message();
        message.setToId(model.getActorId());//谁登陆
        message.setContent("您上次的登陆IP异常(ip异常逻辑没写,邮件提醒还有问题)）");
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap();
        map.put("username", model.getExt("username"));
        System.out.println("发邮件");
        mailSender.sendWithHTMLTemplate(model.getExt("to"), "登陆异常",
                "mails/welcome.html", map);
        System.out.println("发邮件");
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
