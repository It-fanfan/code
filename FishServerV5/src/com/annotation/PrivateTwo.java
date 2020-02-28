package com.annotation;

import java.lang.annotation.*;

/**
 * 私有参数1
 *
 * @author Host-0222
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrivateTwo
{

}
