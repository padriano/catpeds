package com.catpeds.crawler.pawpeds;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector.SelectorParseException;

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

	private static final Pattern URL_ID_PATTERN = Pattern.compile("(.*)id=(\\d+)(.*)");

	/**
	 * Converts a {@link Document} into {@link PedigreeSearchResult}'s.
	 *
	 * @param searchDocument
	 *            HTML document with the search results content
	 * @return a {@link List} of {@link PedigreeSearchResult} representing the
	 *         parsed pedigree results
	 */
	public List<PedigreeSearchResult> parseSearch(Document searchDocument) {

		try {
			// check if an error occurred
			String errorMessage = searchDocument.select("th.error").text();
			if (!isNullOrEmpty(errorMessage)) {
				if (Objects.equal(errorMessage, "Sorry, nothing found")) {
					return Arrays.asList();
				}
				throw new IllegalArgumentException(errorMessage);
			}

			// read the result table
			Elements resultTableRows = searchDocument.select("table.searchresult").get(0).select("tr");
			List<PedigreeSearchResult> pedigreeResults = new ArrayList<>(resultTableRows.size());
			for (Element row : resultTableRows) {
				Elements rowCells = row.select("td");
				if (rowCells.size() == 6) {
					pedigreeResults.add(parseSearchResultRow(rowCells));
				}
			}
			return pedigreeResults;

		} catch (IllegalArgumentException e) {
			throw e;

		} catch (SelectorParseException e) {
			// check if the exception is caused by expected no results message or an error occurred
			String errorMessage = searchDocument.select("th.error").text();
			if (!Objects.equal(errorMessage, "Sorry, nothing found")) {
				throw new IllegalArgumentException(errorMessage);
			}

		} catch (Exception e) {
			System.err.println("Exception while parsing search result with document: \n" + searchDocument.toString());
			e.printStackTrace();
		}

		return Arrays.asList();
	}


	private PedigreeSearchResult parseSearchResultRow(Elements rowCells) {
		Element anchor = rowCells.get(1).select("a").first();
		long id = getIdFromAnchor(anchor);

		String name = anchor.text().trim();
		String title = rowCells.get(0).text().replace("\u00a0", " ").trim();
		String genderAsText = rowCells.get(2).text().trim();
		Gender gender = null;
		if (Objects.equal("M", genderAsText) || Objects.equal("F", genderAsText)) {
			gender = Gender.valueOf(genderAsText);
		}
		String ems = rowCells.get(3).text().replace("\u00a0", " ").trim();
		LocalDate dob = LocalDate.parse(rowCells.get(4).text().replace("\u00a0", " ").trim());

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
}
