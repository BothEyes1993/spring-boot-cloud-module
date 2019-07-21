package com.sbcm.adapter.inject;


import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface AspectLogs {

    String description() default "";

}