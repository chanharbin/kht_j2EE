package com.kht.backend.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLog {

    int value() default 0;
}
