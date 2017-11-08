package com.catpeds.crawler.pawpeds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.io.Files;

/**
 * Unit test class to verify that {@link Mockito} is using the opt-in feature
 * that allows to mock finals. This will be the default MockMaker in Mockito 3.
 *
 * @author padriano
 *
 */
public class MockitoMockMakerConfigTest {

	/**
	 * Test that the Mockito plugin file exist in the classpath
	 */
	@Test
	public void testMockMakerFileInClasspath() throws Exception {
		InputStream stream = Test.class.getResourceAsStream("/mockito-extensions/org.mockito.plugins.MockMaker");
		assertNotNull("The mockito plugin config file for mock maker must exist in the classpath", stream);

		String line = Files.asCharSource(
				new File(Test.class.getResource("/mockito-extensions/org.mockito.plugins.MockMaker").toURI()),
				StandardCharsets.UTF_8).readFirstLine();
		assertEquals("mock-maker-inline", line);
	}

	/**
	 * Test that the opt-in inline maker feature is specified as expected
	 */
	@Test
	public void testMockMakerFileContent() throws Exception {
		String line = Files.asCharSource(
				new File(Test.class.getResource("/mockito-extensions/org.mockito.plugins.MockMaker").toURI()),
				StandardCharsets.UTF_8).readFirstLine();
		assertEquals("mock-maker-inline", line);
	}

	/**
	 * Test that it is indeed possible to mock final classes and final methods
	 */
	@Test
	public void testMockingFinals() {
		FinalClass concrete = new FinalClass();

		FinalClass mock = mock(FinalClass.class);
		when(mock.finalMethod()).thenReturn("not anymore");

		assertNotEquals("Mocked method cannot have the same value", mock.finalMethod(), concrete.finalMethod());
	}

	/**
	 * Auxiliary final class to be used in this test
	 * @author padriano
	 *
	 */
	private final static class FinalClass {
		final String finalMethod() {
			return "something";
		}
	}
}
