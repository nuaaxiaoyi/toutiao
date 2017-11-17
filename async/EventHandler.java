package com.nowcoder.toutiao.async;

import java.util.List;

/**
 * Created by xiaoyy on 11/11/17.
 */
//每个handler处理event都不一样，所以是一个interface
public interface EventHandler {

    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes(); //这个handler还需要关注哪些eventtype


}
