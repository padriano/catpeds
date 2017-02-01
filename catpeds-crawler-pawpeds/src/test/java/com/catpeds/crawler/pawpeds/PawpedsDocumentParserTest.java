package com.catpeds.crawler.pawpeds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector.SelectorParseException;
import org.junit.Before;
import org.junit.Test;

import com.catpeds.model.Pedigree;
import com.catpeds.model.Pedigree.Gender;
import com.catpeds.model.PedigreeSearchResult;
import com.catpeds.model.builder.PedigreeBuilder;
import com.catpeds.model.builder.PedigreeSearchResultBuilder;
import com.catpeds.model.comparator.PedigreeComparator;
import com.catpeds.model.comparator.PedigreeSearchResultComparator;

/**
 * Unit test class for {@link PawpedsDocumentParser}
 *
 * @author padriano
 *
 */
public class PawpedsDocumentParserTest {

	private PawpedsDocumentParser pawpedsDocumentParser;

	/**
	 * Initialisation
	 */
	@Before
	public void setup() {
		pawpedsDocumentParser = new PawpedsDocumentParser();
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parseSearch(Document)} parses a
	 * document with results correctly.
	 */
	@Test
	public void testParseSearchWithFoundResults() throws Exception {
		// Given
		// loading file with 4 results
		Document document = loadJSoupDocument("/pawpeds/4matches.html");

		PedigreeSearchResult firstExpectedResult = new PedigreeSearchResultBuilder().withId(1262490l).withTitle("")
				.withName("Padawan Adi Gallia").withGender(Gender.F).withEms("NFO a 09 24")
				.withDob(LocalDate.of(2015, 4, 15)).build();
		PedigreeSearchResult secondExpectedResult = new PedigreeSearchResultBuilder().withId(1262488l).withTitle("CH")
				.withName("Padawan Alderaan").withGender(Gender.M).withEms("NFO a 03 24")
				.withDob(LocalDate.of(2015, 4, 15)).build();

		// When
		List<PedigreeSearchResult> result = pawpedsDocumentParser.parseSearch(document);

		// Then
		assertEquals("Expecting 4 results", 4, result.size());
		PedigreeSearchResultComparator comparator = new PedigreeSearchResultComparator();
		assertEquals("Unexpected first result", 0, comparator.compare(result.get(0), firstExpectedResult));
		assertEquals("Unexpected second result", 0, comparator.compare(result.get(1), secondExpectedResult));
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parseSearch(Document)} parses a
	 * document with no results correctly.
	 */
	@Test
	public void testParseSearchWithNoResultsFound() throws Exception {
		// Given
		// loading file with no results
		Document document = loadJSoupDocument("/pawpeds/noresults.html");

		// When
		List<PedigreeSearchResult> result = pawpedsDocumentParser.parseSearch(document);

		// Then
		assertTrue("Not expecting results", result.isEmpty());
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parseSearch(Document)} throws an
	 * {@link IllegalArgumentException} if there is unexpected format in the
	 * document.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testParseSearchWithUnexpectedFormat() throws Exception {
		// Given
		// loading file with error
		Document document = loadJSoupDocument("/pawpeds/error.html");

		// When
		pawpedsDocumentParser.parseSearch(document);

		// Then
		// the exception is expected
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parseSearch(Document)} throws an
	 * {@link IllegalArgumentException} if there is an jsoup parsing error.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testJsoupSelectorUnexpectedError() throws Exception {
		// Given
		Document document = mock(Document.class);

		Elements noErrorElement = mock(Elements.class);
		when(noErrorElement.text()).thenReturn("");
		when(document.select("th.error")).thenReturn(noErrorElement);

		when(document.select("table.searchresult tr.searchresult:has(td.searchresult)")).thenThrow(SelectorParseException.class);

		// When
		pawpedsDocumentParser.parseSearch(document);

		// Then
		// the exception is expected
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parseSearch(Document)} throws an
	 * {@link Exception} that isn't {@link IllegalArgumentException} the return
	 * is empty.
	 */
	@Test
	public void testUnexpectedException() throws Exception {
		// Given
		Elements noErrorElement = mock(Elements.class);
		when(noErrorElement.text()).thenThrow(RuntimeException.class);

		Document document = mock(Document.class);
		when(document.select("th.error")).thenReturn(noErrorElement);

		// When
		List<PedigreeSearchResult> result = pawpedsDocumentParser.parseSearch(document);

		// Then
		assertTrue("Not expecting results", result.isEmpty());
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parseOffsprings(Document)} parses a
	 * document with results correctly.
	 */
	@Test
	public void testParseOffspringsWithFoundResults() throws Exception {
		// Given
		// loading file with 4 results
		Document document = loadJSoupDocument("/pawpeds/offsprings.html");

		PedigreeSearchResult firstExpectedResult = new PedigreeSearchResultBuilder().withId(1262490l).withTitle("")
				.withName("Padawan Adi Gallia").withGender(Gender.F).withEms("NFO a 09 24")
				.withDob(LocalDate.of(2015, 4, 15)).build();
		PedigreeSearchResult secondExpectedResult = new PedigreeSearchResultBuilder().withId(1262488l).withTitle("CH")
				.withName("Padawan Alderaan").withGender(Gender.M).withEms("NFO a 03 24")
				.withDob(LocalDate.of(2015, 4, 15)).build();

		// When
		List<PedigreeSearchResult> result = pawpedsDocumentParser.parseOffsprings(document);

		// Then
		assertEquals("Expecting 4 results", 4, result.size());
		PedigreeSearchResultComparator comparator = new PedigreeSearchResultComparator();
		assertEquals("Unexpected first result", 0, comparator.compare(result.get(0), firstExpectedResult));
		assertEquals("Unexpected second result", 0, comparator.compare(result.get(1), secondExpectedResult));
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parsePedigree(Document)} parses a
	 * common pedigree document correctly.
	 */
	@Test
	public void testParsePedigree() throws Exception {
		// Given
		// loading file with correct pedigree
		Document document = loadJSoupDocument("/pawpeds/pedigree.html");

		Pedigree expectedPedigree = new PedigreeBuilder().withId(1207806l)
				.withName("CH Lothiriel de Rivendell's Land").withGender(Gender.F).withInbreeding(4.46).withEms("NFO a 03 22")
				.withDob(LocalDate.of(2013, 12, 26)).withNationalityCountryCode("PT").withLocationCountryCode("GB")
				.withSireId(961651l).withDamId(961652l).build();

		// When
		Optional<Pedigree> pedigree = pawpedsDocumentParser.parsePedigree(document);

		// Then
		assertTrue("Expecting a pedigree", pedigree.isPresent());
		PedigreeComparator comparator = new PedigreeComparator();
		assertEquals("Unexpected pedigree", 0, comparator.compare(expectedPedigree, pedigree.get()));
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parsePedigree(Document)} returns
	 * {@link Optional#empty()} for invalid ids.
	 */
	@Test
	public void testParsePedigreeInvalidId() throws Exception {
		// Given
		// loading file with invalid id pedigree
		Document document = loadJSoupDocument("/pawpeds/invalidPedigreeId.html");

		// When
		Optional<Pedigree> pedigree = pawpedsDocumentParser.parsePedigree(document);

		// Then
		assertFalse("Unexpected pedigree", pedigree.isPresent());
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parsePedigree(Document)} parses a
	 * foundation pedigree document correctly.
	 */
	@Test
	public void testParsePedigreeFoundation() throws Exception {
		// Given
		// loading file with foundation pedigree
		Document document = loadJSoupDocument("/pawpeds/foundationPedigree.html");

		Pedigree expectedPedigree = new PedigreeBuilder().withId(123350l).withName("Sasja").withGender(Gender.F)
				.withInbreeding(0.0).withEms("NFO n 09").withNationalityCountryCode("NO").build();

		// When
		Optional<Pedigree> pedigree = pawpedsDocumentParser.parsePedigree(document);

		// Then
		assertTrue("Expecting a pedigree", pedigree.isPresent());
		PedigreeComparator comparator = new PedigreeComparator();
		assertEquals("Unexpected pedigree", 0, comparator.compare(expectedPedigree, pedigree.get()));
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parsePedigree(Document)} parses a
	 * pedigree document correctly that has a foundation parent.
	 */
	@Test
	public void testParsePedigreeWithFoundationParent() throws Exception {
		// Given
		// loading file with foundation pedigree
		Document document = loadJSoupDocument("/pawpeds/foundationParentPedigree.html");

		Pedigree expectedPedigree = new PedigreeBuilder().withId(385532l).withName("EP & IC Trollsaga's Justin")
				.withGender(Gender.M).withInbreeding(0.0).withEms("NFO n 23").withDob(LocalDate.of(1983, 7, 31))
				.withDamId(126767l).build();

		// When
		Optional<Pedigree> pedigree = pawpedsDocumentParser.parsePedigree(document);

		// Then
		assertTrue("Expecting a pedigree", pedigree.isPresent());
		PedigreeComparator comparator = new PedigreeComparator();
		assertEquals("Unexpected pedigree", 0, comparator.compare(expectedPedigree, pedigree.get()));
	}

	private Document loadJSoupDocument(String filePath) throws Exception {
		File input = new File(PawpedsDocumentParserTest.class.getResource(filePath).toURI());
		return Jsoup.parse(input, "UTF-8");
	}
}
