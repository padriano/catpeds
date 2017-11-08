package com.catpeds.aop;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Caching mechanism for {@link com.catpeds.crawler.jsoup.DocumentRepository}
 * using Guava's thread-safe {@link Cache} with size and timed eviction.
 *
 * @author padriano
 *
 */
@Aspect
@Named
public class DocumentRepositoryCacheAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRepositoryCacheAspect.class);

	private final Cache<String, Object> cache = CacheBuilder.newBuilder()
		    .maximumSize(1000)
		    .expireAfterAccess(60, TimeUnit.MINUTES)
		    .build();

	/**
	 * Invoked around method execution.
	 *
	 * @param joinPoint
	 * @return
	 * @throws ExecutionException
	 */
	@Around("execution(* com.catpeds.crawler.jsoup.DocumentRepository.get(String))")
	public Object around(ProceedingJoinPoint joinPoint) throws ExecutionException {
		final String url = (String) joinPoint.getArgs()[0];
		LOGGER.debug("Retrieving document from URL {}", url);

		Object result = cache.get(url, new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				LOGGER.debug("Result for URL {} not cached", url);
				try {

					// invoke the method
					return joinPoint.proceed();

				} catch (Throwable e) {
					throw new Exception(e); // NOSONAR
				}
			}
		});

		LOGGER.trace("Result document {}", result);
		return result;
	}
}
