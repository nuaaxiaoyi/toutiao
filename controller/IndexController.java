package com.nowcoder.toutiao.controller;


import com.nowcoder.toutiao.aspect.LogAspect;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.service.ToutiaoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * Created by xiaoyy on 11/1/17.
 */
//@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private ToutiaoService toutiaoService;





    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(HttpSession session) {
        logger.info("Visit Index");
        return "Hello this is index, GO!!!!!!!!" + session.getAttribute("msg") + "<br/> say " + toutiaoService.say();
    }




    @RequestMapping(value = {"profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1" ) int type,
                          @RequestParam(value = "key", defaultValue = "nowcoder") String key) {

        return String.format("Group Id{%s}, User Id {%d}, Type{%d}, Key {%s}", groupId, userId, type, key);

    }

    @RequestMapping(path = {"/news"})
    public String news(Model model) {
        model.addAttribute("value1", "vv1");
        List<String> colors = Arrays.asList(new String[] {"Red","Green","Blue"});
        model.addAttribute("color", colors);

        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < 4; i++){
            map.put(String.valueOf(i),String.valueOf(i*i));

        }
        model.addAttribute("map", map);

        User jim = new User("Jim");
        model.addAttribute("user", jim);


        return "news";

    }
    @RequestMapping(path = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headderNames = request.getHeaderNames();
        while(headderNames.hasMoreElements()) {
            String name = headderNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br/>");

        }

        for(Cookie cookie: request.getCookies()) {
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br/>");

        }
        sb.append("getMethod:" + request.getMethod() + "<br/>");
        sb.append("getPathInfo:" + request.getPathInfo() + "<br/>");
        sb.append("getQuerryString:" + request.getQueryString() + "<br/>");
        sb.append("getRequestURI:"+ request.getRequestURI() + "<br/>");

        return sb.toString();
    }

    @RequestMapping(path = {"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "nowcoder", defaultValue = "a") String nowcoder,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response) {
        response.addCookie(new Cookie(key,value));
        response.addHeader(key, value);

        return "NowCoderId From Cookie " + nowcoder;
    }

    @RequestMapping("/redirect/{code}")
    public /*RedirectView*/ String  redirect(@PathVariable("code") int code,
                                             HttpSession session) {
        /*
        RedirectView red = new RedirectView("/",true);
        if(code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }

        return red;
        */
        session.setAttribute("msg", "Jump From Redirect");
        return "redirect:/";   //Simple way for redirecting
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = false) String key) {
        if("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("key wrong!!");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }


}
