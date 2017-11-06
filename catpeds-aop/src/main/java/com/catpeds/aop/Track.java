package com.catpeds.aop;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method to be tracked.
 *
 * @author padriano
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Track {

}
