package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 避免重复提交
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidRepeatableCommit
{

    /**
     * 指定时间内不可重复提交,单位毫秒
     *
     * @return time out millis
     */
    long timeout() default 300;

    /**
     * 指定拦截
     */
    boolean filter() default true;

}