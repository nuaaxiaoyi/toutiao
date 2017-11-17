package com.nowcoder.toutiao.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * Created by xiaoyy on 11/1/17.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @Before("execution(* com.nowcoder.toutiao.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint /*切点*/) {
        StringBuilder sb = new StringBuilder();
        for(Object arg : joinPoint.getArgs()) {
            sb.append("arg: " + arg.toString());
        }
        logger.info("before time: " + new Date());  //面上切面编程
        logger.info("before method：" + sb.toString());
    }

    @After("execution(* com.nowcoder.toutiao.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint ) {
        logger.info("after method： ");

    }
}
