package com.fish.utils;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Comments
{
    String name() default "";

    String option() default "";

    String formatter() default "";

    String editor() default "";
}
