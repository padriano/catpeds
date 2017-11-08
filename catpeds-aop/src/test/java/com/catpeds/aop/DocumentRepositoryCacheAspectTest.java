package com.catpeds.aop;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit test class for {@link DocumentRepositoryCacheAspect}
 *
 * @author padriano
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentRepositoryCacheAspectTest {

	@Mock
	private ProceedingJoinPoint joinPoint;

	@InjectMocks
	private DocumentRepositoryCacheAspect documentRepositoryCacheAspect;

	/**
	 * Check that {@link DocumentRepositoryCacheAspect#around(ProceedingJoinPoint)}
	 * caches the invocation result.
	 */
	@Test
	public void testResultsAreCached() throws Throwable {
		// Given
		Object expectedResult = mock(Object.class);
		when(joinPoint.getArgs()).thenReturn(new Object[] {"http://foo.bar"});
		when(joinPoint.proceed()).thenReturn(expectedResult);

		DocumentRepositoryCacheAspect documentRepositoryCacheAspect = new DocumentRepositoryCacheAspect();

		// When
		Object firstResult = documentRepositoryCacheAspect.around(joinPoint);
		Object secondResult = documentRepositoryCacheAspect.around(joinPoint);

		// Then
		assertSame("Expecting the result from the join point execution", expectedResult, firstResult);
		assertSame("Expecting the result from the join point execution", expectedResult, secondResult);
		verify(joinPoint).proceed(); // when cached this method should only be invoked on the first invocation
		verify(joinPoint, times(2)).getArgs();
	}

	/**
	 * Check that {@link DocumentRepositoryCacheAspect#around(ProceedingJoinPoint)}
	 * wraps the join point throwables into {@link ExecutionException}
	 */
	@Test(expected = ExecutionException.class)
	public void testThrowablesAreWrappedInExecutionException() throws Throwable {
		// Given
		when(joinPoint.getArgs()).thenReturn(new Object[] {"http://foo.bar"});
		when(joinPoint.proceed()).thenThrow(Exception.class);

		// When
		documentRepositoryCacheAspect.around(joinPoint);

		// Then
		// Exception is expected to be thrown
	}
}
