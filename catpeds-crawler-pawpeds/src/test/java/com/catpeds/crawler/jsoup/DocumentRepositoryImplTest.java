package com.catpeds.crawler.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * {@link DocumentRepositoryImpl} unit test class.
 *
 * <p>
 * This unit test is ignored due to the PowerMock incompatibility with the
 * latest Mockito version. Once this is fixed this test will be ready to be run
 * to add coverage
 * </p>
 *
 * @author padriano
 *
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class DocumentRepositoryImplTest {

	@Mock
	private Connection connection;
	@Mock
	private Document document;

	private DocumentRepositoryImpl repository;

	/**
	 * Initialisation
	 */
	@Before
	public void setup() {
		repository = new DocumentRepositoryImpl();
	}

	/**
	 * Test that {@link DocumentRepositoryImpl#get(String)} returns
	 * an {@link Optional} with the {@link Document} when a connection is
	 * possible.
	 */
	@Test
	public void testGetingDocument() throws IOException {
		// Given
		String url = "http://example.com";
		when(Jsoup.connect(url)).thenReturn(connection);
		when(connection.get()).thenReturn(document);

		// When
		Optional<Document> result = repository.get(url);

		// Then
		assertTrue(result.isPresent());
		assertEquals("Expecting the same document that was mocked", document, result.get());
	}

	/**
	 * Test that {@link DocumentRepositoryImpl#get(String)} returns
	 * an empty {@link Optional} when a connection timeout occur.
	 */
	@Test
	public void testGetingDocumentTimeout() throws IOException {
		// Given
		String url = "http://example.com";
		when(Jsoup.connect(url)).thenReturn(connection);
		when(connection.get()).thenThrow(SocketTimeoutException.class);

		// When
		Optional<Document> result = repository.get(url);

		// Then
		assertFalse("Not expecting a document", result.isPresent());
	}

	/**
	 * Test that {@link DocumentRepositoryImpl#get(String)} propagates
	 * the {@link IOException}s.
	 */
	@Test(expected = IOException.class)
	public void testGetingDocumentIOExceptionOcurr() throws IOException {
		// Given
		String url = "http://example.com";
		when(Jsoup.connect(url)).thenReturn(connection);
		when(connection.get()).thenThrow(IOException.class);

		// When
		repository.get(url);

		// Then
		// the exception is expected to have been thrown
	}
}
