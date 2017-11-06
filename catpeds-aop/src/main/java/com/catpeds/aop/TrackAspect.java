package com.catpeds.aop;

import javax.inject.Named;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect that logs the methods invocations annotated with {@link Track}
 *
 * @author padriano
 *
 */
@Aspect
@Named
class TrackAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrackAspect.class);

	/**
	 * Invoked before method invocation.
	 *
	 * @param joinPoint
	 */
	@Before("@annotation(com.catpeds.aop.Track)")
	public void before(JoinPoint joinPoint) {
		LOGGER.info("Entering {} with args {}", joinPoint, joinPoint.getArgs());
	}

	/**
	 * Invoked after method returns result.
	 *
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(value = "@annotation(com.catpeds.aop.Track)", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		LOGGER.info("Exiting {} with result {}", joinPoint, result);
	}

	/**
	 * Invoked after method throwing.
	 *
	 * @param joinPoint
	 * @param throwable
	 */
	@AfterThrowing(value = "@annotation(com.catpeds.aop.Track)", throwing = "throwable")
	public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
		LOGGER.error("Exiting {} with message {} and stack trace {}", joinPoint, throwable.getMessage(), throwable.getStackTrace());
	}

}
