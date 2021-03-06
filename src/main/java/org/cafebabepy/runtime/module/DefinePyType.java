package org.cafebabepy.runtime.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yotchang4s on 2017/05/12.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DefinePyType {

    String name();

    String[] parent() default {"builtins.object"};

    boolean appear() default true;
}
