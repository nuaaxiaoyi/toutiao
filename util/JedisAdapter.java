package com.nowcoder.toutiao.util;

import com.alibaba.fastjson.JSON;
import com.nowcoder.toutiao.aspect.LogAspect;
import com.qiniu.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;

/**
 * Created by xiaoyy on 11/10/17.
 */
@Service
public class JedisAdapter implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private JedisPool pool = null;


    public static void print(int index, Object obj) {
        System.out.println(String.format("%d,%s", index, obj.toString()));
    }

/* redis 练习
    public static void main(String[] arg) {
        Jedis jedis = new Jedis(); //自动连接本机6379 端口
        jedis.flushAll();//全删掉

        jedis.set("hello", "word");
        print(1, jedis.get("hello"));
        jedis.rename("hello","newhello");
        print(2, jedis.get("newhello"));
        jedis.setex("hello2",  15, "word");  //设置15秒过期时间

        // 例如，评论数在增加的时候，把增加的数先存在内存里面，只有在刷新页面的时候再保存到数据库
        // 改善高并发量时候的服务器压力
        // 适用于数值型数据
        jedis.set("pv", "100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        jedis.incrBy("pv", 7);
        print(2, jedis.get("pv"));

        // 队列操作  ,底层数据结构是一个双向链表
        String listName = "list";
        for(int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(3, jedis.lrange(listName, 0, 12));
        print(4, jedis.llen(listName));
        print(5, jedis.lpop(listName));  //返回pop出来的那个数
        print(6, jedis.llen(listName));
        print(7, jedis.lindex(listName, 3));
        jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4","xx");  //插入前面
        jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4","yy");  //插入后面
        print(10, jedis.lrange(listName, 0, 15));


        //不定类的属性，可以用hash来存储
        //随时可以添加新的属性
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "12212");

        print(12, jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey)); // 把所有属性get出来
        jedis.hdel(userKey, "phone");  //删掉phone
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hkeys(userKey));
        print(16, jedis.hvals(userKey));
        print(17, jedis.hexists(userKey,"email"));
        print(18, jedis.hexists(userKey, "age"));
        jedis.hsetnx(userKey, "school", "zzz"); //如果存在了就不再set, 这样不用get出来
        jedis.hsetnx(userKey, "name", "sdasd");
        print(19, jedis.hgetAll(userKey));

        //集合 set
        String likeKey1 = "newsLike1";
        String likeKey2 = "newsLike2";
        for (int i = 0; i < 10; i++){
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*2));
        }
        print(20, jedis.smembers(likeKey1));
        print(21, jedis.smembers(likeKey2));
        print(22, jedis.sinter(likeKey1, likeKey2)); //交集
        print(23, jedis.sunion(likeKey1, likeKey2)); //并
        print(24, jedis.sdiff(likeKey1, likeKey2)); // 我有你没有的
        print(25, jedis.sismember(likeKey1, "5"));
        jedis.srem(likeKey1, "5");
        print(26, jedis.sismember(likeKey1, "5"));
        print(27, jedis.scard(likeKey1)); //求长度
        jedis.smove(likeKey2, likeKey1, "14");
        print(28, jedis.scard(likeKey1));
        print(29, jedis.smembers(likeKey1));

        // sort ， 类似优先队列heap
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15 , "jim");
        jedis.zadd(rankKey, 60 , "Ben");
        jedis.zadd(rankKey, 90 , "Lee");
        jedis.zadd(rankKey, 80 , "Mei");
        jedis.zadd(rankKey, 75 , "Lucy");
        print(30, jedis.zcard(rankKey));
        print(31, jedis.zcount(rankKey, 61, 100)); //查看某个区间内有多少个人
        print(32, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Lucy");
        print(33, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Luc"); //会把不存在的值加进去, 并且把2赋给他
        print(34, jedis.zcount(rankKey, 0, 100));
        print(35, jedis.zrange(rankKey, 1, 3));//默认从小到大排序
        print(36, jedis.zrevrange(rankKey, 1, 3));

        for(Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "0", "100")){
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }//按分数把每个人打印出来

        print(38, jedis.zrank(rankKey, "Ben"));  //直接知道ben的排名
        print(39, jedis.zrevrank(rankKey, "Ben")); //反向排名

        JedisPool pool = new JedisPool();
        for(int i = 0; i < 100; i++){
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL" + i);
            j.close();
        }
    }

    */

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379); //把pool初始化
    }

    private Jedis getJedis() {
        return pool.getResource();
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return  jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            jedis.close();
        }

    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return  jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            jedis.close();
        }

    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return  jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            jedis.close();
        }

    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            jedis.close();
        }
    }

    public void setex(String key, String value) {
        // 验证码, 防机器注册，记录上次注册时间，有效期3天
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, 10, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if(value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }


}
