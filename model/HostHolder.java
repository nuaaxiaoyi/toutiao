package com.nowcoder.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * Created by xiaoyy on 11/3/17.
 */
@Component
public class HostHolder {
    //每条线程set进来后，只能get自己线程的部分
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }

}
