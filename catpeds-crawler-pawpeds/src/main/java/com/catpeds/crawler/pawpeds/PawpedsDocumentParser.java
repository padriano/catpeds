package com.catpeds.crawler.pawpeds;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector.SelectorParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.catpeds.model.Pedigree.Gender;
import com.catpeds.model.PedigreeSearchResult;
import com.catpeds.model.builder.PedigreeSearchResultBuilder;
import com.google.common.base.Objects;

/**
 * Service to parse PawPeds HTML documents into POJO representations.
 *
 * @author padriano
 *
 */
class PawpedsDocumentParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(PawpedsDocumentParser.class);

	private static final Pattern URL_ID_PATTERN = Pattern.compile("(.*)id=(\\d+)(.*)");

	/**
	 * Converts a search {@link Document} into {@link PedigreeSearchResult}'s.
	 *
	 * @param searchDocument
	 *            HTML document with the search results content
	 * @return a {@link List} of {@link PedigreeSearchResult} representing the
	 *         parsed pedigree results
	 */
	public List<PedigreeSearchResult> parseSearch(Document searchDocument) {
		return parseSearchTableRows(searchDocument, "table.searchresult tr.searchresult:has(td.searchresult)");
	}

	private List<PedigreeSearchResult> parseSearchTableRows(Document document, String rowsJQuery) {

		try {
			// check if an error occurred
			String errorMessage = document.select("th.error").text();
			if (!isNullOrEmpty(errorMessage)) {
				if (Objects.equal(errorMessage, "Sorry, nothing found")) {
					return Arrays.asList();
				}
				throw new IllegalArgumentException(errorMessage);
			}

			// read the result table
			Elements resultTableRows = document.select(rowsJQuery);
			return resultTableRows.stream().map(row -> parseSearchResultRow(row.select("td"))).collect(Collectors.toList());

		} catch (SelectorParseException e) {
			// check if the exception is caused by expected no results message or an error occurred
			String errorMessage = document.select("th.error").text();
			if (!Objects.equal(errorMessage, "Sorry, nothing found")) {
				throw new IllegalArgumentException(errorMessage, e);
			}

		} catch (IllegalArgumentException e) {
			// catch and re-throw so it isn't caught in next block
			throw e;

		} catch (Exception e) {
			LOGGER.error("Unexpected exception occurred. {}", e);
			LOGGER.error("Error while parsing search result for document: \n{}", document);
		}

		return Arrays.asList();
	}


	private PedigreeSearchResult parseSearchResultRow(Elements rowCells) {
		Element anchor = rowCells.get(1).select("a").first();
		long id = getIdFromAnchor(anchor);

		String name = anchor.text().trim();
		final String nbsp = "\u00a0";
		String title = rowCells.get(0).text().replace(nbsp, " ").trim();
		String genderAsText = rowCells.get(2).text().trim();
		Gender gender = null;
		if (Objects.equal("M", genderAsText) || Objects.equal("F", genderAsText)) {
			gender = Gender.valueOf(genderAsText);
		}
		String ems = rowCells.get(3).text().replace(nbsp, " ").trim();
		LocalDate dob = LocalDate.parse(rowCells.get(4).text().replace(nbsp, " ").trim());

		return new PedigreeSearchResultBuilder().withId(id).withName(name).withGender(gender).withTitle(title)
				.withEms(ems).withDob(dob).build();
	}


	private long getIdFromAnchor(Element anchor) {
		String link = anchor.attr("href");
		Matcher idMatcher = URL_ID_PATTERN.matcher(link);
		if (!idMatcher.matches()) {
			throw new IllegalArgumentException(link);
		}
		return Long.valueOf(idMatcher.group(2));
	}

	/**
	 * Converts an offsprings {@link Document} into {@link PedigreeSearchResult}'s.
	 *
	 * @param offspringsDocument
	 *            HTML document with the offspring results content
	 * @return a {@link List} of {@link PedigreeSearchResult} representing the
	 *         parsed pedigree results
	 */
	public List<PedigreeSearchResult> parseOffsprings(Document offspringsDocument) {
		return parseSearchTableRows(offspringsDocument, "table.offspring tr:has(td.offspring)");
	}
}
