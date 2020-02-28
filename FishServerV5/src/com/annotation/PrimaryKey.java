package com.annotation;

import java.lang.annotation.*;

/**
 * 定义注释，主键
 *
 * @author Host-0222
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrimaryKey
{
}
