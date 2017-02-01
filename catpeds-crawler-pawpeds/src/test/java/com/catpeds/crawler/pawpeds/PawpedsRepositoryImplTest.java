package com.catpeds.crawler.pawpeds;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.catpeds.crawler.jsoup.DocumentRepository;
import com.catpeds.model.Pedigree;
import com.catpeds.model.PedigreeSearchCriteria;
import com.catpeds.model.PedigreeSearchResult;
import com.google.common.collect.Iterables;

/**
 * Unit test class for {@link PawpedsRepositoryImpl}
 *
 * @author padriano
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PawpedsRepositoryImplTest {

	@Mock
	private DocumentRepository documentRepository;
	@Mock
	private PawpedsUrlService pawpedsUrlService;
	@Mock
	private PawpedsDocumentParser pawpedsSearchResultParser;
	@Mock
	private PedigreeSearchCriteria criteria;

	private PawpedsRepositoryImpl pawpedsRepository;

	/**
	 * Initialisation
	 */
	@Before
	public void setup() {
		pawpedsRepository = new PawpedsRepositoryImpl(documentRepository, pawpedsUrlService, pawpedsSearchResultParser);
	}

	/**
	 * Test that {@link PawpedsRepositoryImpl#findAll(PedigreeSearchCriteria)}
	 * delegates the URL calculation, HTML document retrieval and the document
	 * parsing to its dependencies. Also the document retrieval is only done one
	 * time (no time out so no retries).
	 */
	@Test
	public void testFindAllSuccessfulGetFirstTime() throws IOException {
		// Given
		String searchUrl = "http://foo.bar";
		when(pawpedsUrlService.getAdvancedSearchUrl(criteria)).thenReturn(searchUrl);

		// it will find a document in the first invocation
		Document document = mock(Document.class);
		when(documentRepository.get(searchUrl)).thenReturn(Optional.of(document));
		// return a search result
		PedigreeSearchResult pedigreeSearchResult = mock(PedigreeSearchResult.class);
		when(pawpedsSearchResultParser.parseSearch(document)).thenReturn(Arrays.asList(pedigreeSearchResult));

		// When
		Collection<PedigreeSearchResult> result = pawpedsRepository.findAll(criteria);

		// Then
		assertEquals("Expecting one search result", 1, result.size());
		assertEquals("Expecting one search result", pedigreeSearchResult, Iterables.getOnlyElement(result));
		// check that there was only one invocation to the document repository/ no retry
		verify(documentRepository).get(searchUrl);
		verify(pawpedsSearchResultParser).parseSearch(document);
	}

	/**
	 * Test that {@link PawpedsRepositoryImpl#findAll(PedigreeSearchCriteria)}
	 * delegates the URL calculation, HTML document retrieval and the document
	 * parsing to its dependencies. Also the document retrieval is done twice
	 * due to a timeout on the document retrieval.
	 */
	@Test
	public void testFindAllSuccessfulGetWithRetry() throws IOException {
		// Given
		String searchUrl = "http://foo.bar";
		when(pawpedsUrlService.getAdvancedSearchUrl(criteria)).thenReturn(searchUrl);

		// it will find a document in the second invocation
		Document document = mock(Document.class);
		when(documentRepository.get(searchUrl)).thenReturn(Optional.empty()).thenReturn(Optional.of(document));
		// return a search result
		PedigreeSearchResult pedigreeSearchResult = mock(PedigreeSearchResult.class);
		when(pawpedsSearchResultParser.parseSearch(document)).thenReturn(Arrays.asList(pedigreeSearchResult));

		// When
		Collection<PedigreeSearchResult> result = pawpedsRepository.findAll(criteria);

		// Then
		assertEquals("Expecting one search result", 1, result.size());
		assertEquals("Expecting one search result", pedigreeSearchResult, Iterables.getOnlyElement(result));
		// check that there was 2 invocations to the document repository/with retry
		verify(documentRepository, times(2)).get(searchUrl);
		verify(pawpedsSearchResultParser).parseSearch(document);
	}

	/**
	 * Test that {@link PawpedsRepositoryImpl#findAll(PedigreeSearchCriteria)}
	 * delegates the URL calculation, HTML document retrieval and the document
	 * parsing to its dependencies. Also the document retrieval is done twice
	 * due to a timeout on the document retrieval but with no success and no
	 * interactions with the parser.
	 */
	@Test
	public void testFindAllUnsuccessfulWithRetry() throws IOException {
		// Given
		String searchUrl = "http://foo.bar";
		when(pawpedsUrlService.getAdvancedSearchUrl(criteria)).thenReturn(searchUrl);

		// it will not find a document
		when(documentRepository.get(searchUrl)).thenReturn(Optional.empty());

		// When
		Collection<PedigreeSearchResult> result = pawpedsRepository.findAll(criteria);

		// Then
		assertTrue("Not expecting search results", result.isEmpty());
		// check that there was only one invocation to the document repository/ no retry
		// check that there was 2 invocations to the document repository/with retry
		verify(documentRepository, times(2)).get(searchUrl);
		verifyZeroInteractions(pawpedsSearchResultParser);
	}

	/**
	 * Test that {@link PawpedsRepositoryImpl#findAll(PedigreeSearchCriteria)}
	 * returns an empty collection when an IOException occurs.
	 */
	@Test
	public void testFindAllWithUnexpectedException() throws IOException {
		// Given
		String searchUrl = "http://foo.bar";
		when(pawpedsUrlService.getAdvancedSearchUrl(criteria)).thenReturn(searchUrl);

		// it will not find a document
		when(documentRepository.get(searchUrl)).thenThrow(IOException.class);

		// When
		Collection<PedigreeSearchResult> result = pawpedsRepository.findAll(criteria);

		// Then
		assertTrue("Not expecting search results", result.isEmpty());
		// check that there was only one invocation to the document repository/
		// no retry due to exception being thrown
		verify(documentRepository).get(searchUrl);
		verifyZeroInteractions(pawpedsSearchResultParser);
	}

	/**
	 * Test that {@link PawpedsRepositoryImpl#findAllOffspring(long)}
	 * delegates the URL calculation, HTML document retrieval and the document
	 * parsing to its dependencies (offspring search).
	 */
	@Test
	public void testFindAllOffspringSuccessful() throws IOException {
		// Given
		String searchUrl = "http://foo.bar";
		long id = 1212;
		when(pawpedsUrlService.getOffspringsSearchUrl(id)).thenReturn(searchUrl);

		// it will find a document in the first invocation
		Document document = mock(Document.class);
		when(documentRepository.get(searchUrl)).thenReturn(Optional.of(document));
		// return a search result
		PedigreeSearchResult offspringSearchResult = mock(PedigreeSearchResult.class);
		when(pawpedsSearchResultParser.parseOffsprings(document)).thenReturn(Arrays.asList(offspringSearchResult));

		// When
		Collection<PedigreeSearchResult> result = pawpedsRepository.findAllOffspring(id);

		// Then
		assertEquals("Expecting one search result", 1, result.size());
		assertEquals("Expecting one search result", offspringSearchResult, Iterables.getOnlyElement(result));
		// check that there was only one invocation to the document repository
		verify(documentRepository).get(searchUrl);
		verify(pawpedsSearchResultParser).parseOffsprings(document);
	}

	/**
	 * Test that {@link PawpedsRepositoryImpl#findAllOffspring(long)}
	 * returns empty result on document retrieval timeout.
	 */
	@Test
	public void testFindAllOffspringTimeout() throws IOException {
		// Given
		String searchUrl = "http://foo.bar";
		long id = 1212;
		when(pawpedsUrlService.getOffspringsSearchUrl(id)).thenReturn(searchUrl);

		// it will not find a document
		when(documentRepository.get(searchUrl)).thenReturn(Optional.empty());

		// When
		Collection<PedigreeSearchResult> result = pawpedsRepository.findAllOffspring(id);

		// Then
		assertTrue("Expecting no search result", result.isEmpty());
		// check that there was a retry
		verify(documentRepository, times(2)).get(searchUrl);
		verifyZeroInteractions(pawpedsSearchResultParser);
	}

	/**
	 * Test that {@link PawpedsRepositoryImpl#findAllOffspring(long)}
	 * returns an empty collection when an IOException occurs.
	 */
	@Test
	public void testFindAllOffspringWithUnexpectedException() throws IOException {
		// Given
		String searchUrl = "http://foo.bar";
		long id = 1212;
		when(pawpedsUrlService.getOffspringsSearchUrl(id)).thenReturn(searchUrl);

		// it will not find a document
		when(documentRepository.get(searchUrl)).thenThrow(IOException.class);

		// When
		Collection<PedigreeSearchResult> result = pawpedsRepository.findAllOffspring(id);

		// Then
		assertTrue("Expecting no search result", result.isEmpty());
		// check that there was only one invocation to the document repository
		verify(documentRepository).get(searchUrl);
		verifyZeroInteractions(pawpedsSearchResultParser);
	}

	/**
	 * Test that {@link PawpedsRepositoryImpl#findAllOffspring(long)}
	 * delegates the URL calculation, HTML document retrieval and the document
	 * parsing to its dependencies (offspring search).
	 */
	@Test
	public void testFindOneWithOffspring() throws IOException {
		// Given
		String searchUrl = "http://foo.bar";
		long id = 1212, offspringId = 432;
		when(pawpedsUrlService.getOffspringsSearchUrl(id)).thenReturn(searchUrl);
		when(pawpedsUrlService.getPedigreeUrl(id)).thenReturn(searchUrl);

		// it will find a document in the first invocation
		Document document = mock(Document.class);
		when(documentRepository.get(searchUrl)).thenReturn(Optional.of(document));
		// return a search result
		PedigreeSearchResult offspringSearchResult = mock(PedigreeSearchResult.class);
		when(offspringSearchResult.getId()).thenReturn(offspringId);
		when(pawpedsSearchResultParser.parseOffsprings(document)).thenReturn(Arrays.asList(offspringSearchResult));
		// expected pedigree
		Pedigree expectedPedigree = new Pedigree();
		expectedPedigree.setId(id);
		when(pawpedsSearchResultParser.parsePedigree(document)).thenReturn(Optional.of(expectedPedigree));

		// When
		Optional<Pedigree> pedigree = pawpedsRepository.findOne(id);

		// Then
		assertTrue("Expecting one search result", pedigree.isPresent());
		assertEquals("Unexpected pedigree", id, pedigree.get().getId());
		assertEquals("Expecting one offspring", offspringId, getOnlyElement(pedigree.get().getOffsprings()).longValue());
		verify(documentRepository, times(2)).get(searchUrl); // one for offsprings and one for pedigree
		verify(pawpedsUrlService).getOffspringsSearchUrl(id);
	}
}
