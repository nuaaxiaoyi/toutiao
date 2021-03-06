package com.nowcoder.toutiao.service;

import com.nowcoder.toutiao.dao.LoginTicketDAO;
import com.nowcoder.toutiao.dao.UserDAO;
import com.nowcoder.toutiao.model.LoginTicket;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by xiaoyy on 11/1/17.
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isEmpty(username)) {
            map.put("magname", "用户名不能为空");
            return  map;
        }
        if(StringUtils.isEmpty(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        if(userDAO.selectByName(username) != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }
        //注册用户

        User user = new User();

        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));

        userDAO.addUser(user);

        //登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;

    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isEmpty(username)) {
            map.put("magname", "用户名不能为空");
            return  map;
        }
        if(StringUtils.isEmpty(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if( user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if(!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }

        map.put("userId", user.getId());


        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;

    }
    //登陆成功
    //给用户下发一个ticket
    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();

    }



    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
