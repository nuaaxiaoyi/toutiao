package com.nowcoder.toutiao.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by xiaoyy on 11/2/17.
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }

}
