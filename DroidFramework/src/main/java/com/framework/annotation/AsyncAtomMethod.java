package com.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AsyncAtomMethod {

    enum ArgType {
        normal,
        atom
    }

    ArgType methodType() default ArgType.atom;
    boolean withDialog() default false;
    boolean withCancelableDialog() default false;
    String withMessage() default "正在加载，请稍后...";

}
