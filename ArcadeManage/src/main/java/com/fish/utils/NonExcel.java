package com.fish.utils;

import java.lang.annotation.*;

/**
 * 不导出Excel的列
 *
 * @author Host-0222
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonExcel
{

}
