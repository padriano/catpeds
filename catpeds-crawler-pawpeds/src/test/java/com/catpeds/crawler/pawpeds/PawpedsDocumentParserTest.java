package com.catpeds.crawler.pawpeds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import com.catpeds.model.Pedigree.Gender;
import com.catpeds.model.PedigreeSearchResult;
import com.catpeds.model.builder.PedigreeSearchResultBuilder;
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
		File input = new File(Test.class.getResource("/pawpeds/4matches.html").toURI());
		Document document = Jsoup.parse(input, "UTF-8");

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
		File input = new File(Test.class.getResource("/pawpeds/noresults.html").toURI());
		Document document = Jsoup.parse(input, "UTF-8");

		// When
		List<PedigreeSearchResult> result = pawpedsDocumentParser.parseSearch(document);

		// Then
		assertTrue("Not expecting results", result.isEmpty());
	}

	/**
	 * Test that {@link PawpedsDocumentParser#parseSearch(Document)} throws an
	 * {@link IllegalArgumentException} if there is an error in the document
	 * format.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testParseSearchWithError() throws Exception {
		// Given
		// loading file with error
		File input = new File(Test.class.getResource("/pawpeds/error.html").toURI());
		Document document = Jsoup.parse(input, "UTF-8");

		// When
		pawpedsDocumentParser.parseSearch(document);

		// Then
		// the exception is expected
	}
}
