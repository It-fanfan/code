package com.annotation;

import java.lang.annotation.*;

/**
 * 只读属性
 *
 * @author Host-0222
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadOnly
{

}
