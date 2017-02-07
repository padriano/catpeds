package com.catpeds.crawler.pawpeds;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector.SelectorParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.catpeds.model.Pedigree;
import com.catpeds.model.Pedigree.Gender;
import com.catpeds.model.builder.PedigreeBuilder;
import com.google.common.base.Objects;

/**
 * Service to parse PawPeds HTML documents into POJO representations.
 *
 * @author padriano
 *
 */
@Named
class PawpedsDocumentParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(PawpedsDocumentParser.class);

	private static final Pattern URL_ID_PATTERN = Pattern.compile("(.*)id=(\\d+)(.*)");

	private static final Pattern LOCATION_PATTERN = Pattern.compile("(.*)(\\w\\w|-)/(\\w\\w|-)(.*)");

	private static final Pattern INBREEDING_PATTERN = Pattern.compile("(.*)inbreeding = (.*)%(.*)");

	private static final Pattern DOB_PATTERN = Pattern.compile("(.*)(\\d{4}-\\d{2}-\\d{2})(.*)");

	private static final Pattern NAME_PATTERN = Pattern.compile("(.*?),(.*)");

	/**
	 * Converts a search {@link Document} into {@link Pedigree}'s.
	 *
	 * @param searchDocument
	 *            HTML document with the search results content
	 * @return a {@link List} of {@link Pedigree} representing the
	 *         parsed pedigree results
	 */
	public List<Pedigree> parseSearch(Document searchDocument) {
		return parseSearchTableRows(searchDocument, "table.searchresult tr.searchresult:has(td.searchresult)");
	}

	/**
	 * Converts an offsprings {@link Document} into {@link Pedigree}'s.
	 *
	 * @param offspringsDocument
	 *            HTML document with the offspring results content
	 * @return a {@link List} of {@link Pedigree} representing the
	 *         parsed pedigree results
	 */
	public List<Pedigree> parseOffsprings(Document offspringsDocument) {
		return parseSearchTableRows(offspringsDocument, "table.offspring tr:has(td.offspring)");
	}

	private List<Pedigree> parseSearchTableRows(Document document, String rowsJQuery) {

		try {
			// check if an error occurred
			String errorMessage = document.select("th.error").text();
			if (!isNullOrEmpty(errorMessage)) {
				if (Objects.equal(errorMessage, "Sorry, nothing found")) {
					return asList();
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

		return asList();
	}

	private Pedigree parseSearchResultRow(Elements rowCells) {
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

		return new PedigreeBuilder().withId(id).withName(name).withGender(gender).withTitle(title)
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
	 * Converts an offsprings {@link Document} into {@link Pedigree}'s.
	 *
	 * @param offspringsDocument
	 *            HTML document with the offspring results content
	 * @return a {@link List} of {@link Pedigree} representing the
	 *         parsed pedigree results
	 */
	public Optional<Pedigree> parsePedigree(Document pedigreeDocument) {

		Elements pedigreeTable = pedigreeDocument.select("table.pedigree");
		if (pedigreeTable.isEmpty()) {
			// there is no pedigree for the specified id
			LOGGER.info("Pedigree not found for the specified id");
			return Optional.empty();
		}

		Pedigree pedigree = new Pedigree();
		// parse id
		pedigree.setId(getIdFromAnchor(pedigreeDocument.select("p.subnavbar a").first()));
		pedigree.setDob(getDob(pedigreeDocument));
		pedigree.setName(getName(pedigreeDocument));
		pedigree.setEms(getEms(pedigreeDocument));
		pedigree.setGender(getGender(pedigreeDocument));
		pedigree.setNationalityCountryCode(getNationalityCountryCode(pedigreeDocument));
		pedigree.setLocationCountryCode(getLocationyCountryCode(pedigreeDocument));
		pedigree.setInbreeding(getInbreeding(pedigreeDocument));

		// set parent id's
		getParentId(pedigreeDocument, Gender.M).ifPresent(pedigree::setSireId);
		getParentId(pedigreeDocument, Gender.F).ifPresent(pedigree::setDamId);

		LOGGER.debug("Pedigree found {}", pedigree);
		return Optional.of(pedigree);
	}

	double getInbreeding(Document pedigreeDocument) {
		String inbreedingHeader = pedigreeDocument.select("table.pedigree td.pedigree_center").text();
		Matcher inbreedingMatcher = INBREEDING_PATTERN.matcher(inbreedingHeader);
		return inbreedingMatcher.matches() ? Double.parseDouble(inbreedingMatcher.group(2)) : 0.0;
	}

	String getNationalityCountryCode(Document pedigreeDocument) {
		Matcher locationMatcher = LOCATION_PATTERN.matcher(getPedigreeDocumentHeaderText(pedigreeDocument));
		if (locationMatcher.matches()) {
			String countryCode = locationMatcher.group(2);
			return Objects.equal("-", countryCode) ? null : countryCode; // "-" is used when unavailable code
		}
		return locationMatcher.matches() ? locationMatcher.group(2) : null;
	}

	String getLocationyCountryCode(Document pedigreeDocument) {
		Matcher locationMatcher = LOCATION_PATTERN.matcher(getPedigreeDocumentHeaderText(pedigreeDocument));
		if (locationMatcher.matches()) {
			String countryCode = locationMatcher.group(3);
			return Objects.equal("-", countryCode) ? null : countryCode; // "-" is used when unavailable code
		}
		return null;
	}

	Gender getGender(Document pedigreeDocument) {
		String pedigreeDocumentHeader = getPedigreeDocumentHeaderText(pedigreeDocument);
		if (pedigreeDocumentHeader.contains(", F,")) {
			return Gender.F;
		}
		if (pedigreeDocumentHeader.contains(", M,")) {
			return Gender.M;
		}
		return null;
	}

	String getEms(Document pedigreeDocument) {
		String pedigreeDocumentHeader = getPedigreeDocumentHeaderText(pedigreeDocument);
		return asList(pedigreeDocumentHeader.split(",")).stream().filter(s -> s.contains("NFO")).findFirst().orElse("").trim();
	}

	String getName(Document pedigreeDocument) {
		Matcher nameMatcher = NAME_PATTERN.matcher(getPedigreeDocumentHeaderText(pedigreeDocument));
		return nameMatcher.matches() ? nameMatcher.group(1) : null;
	}

	LocalDate getDob(Document pedigreeDocument) {
		Matcher dobMatcher = DOB_PATTERN.matcher(getPedigreeDocumentHeaderText(pedigreeDocument));
		return dobMatcher.matches() ? LocalDate.parse(dobMatcher.group(2)) : null;
	}

	String getPedigreeDocumentHeaderText(Document pedigreeDocument) {
		return pedigreeDocument.select("table.pedigree th.pedigree").text().replace("\u00a0", " ").trim();
	}

	Optional<Long> getParentId(Document pedigreeDocument, Gender gender) {
		Elements parentsDocument = pedigreeDocument.select("table.pedigree td.pedigree:has(span.siredamInPedigree)");
		return parentsDocument.stream()
			.filter(td -> Objects.equal(td.getElementsByTag("span").text().trim(), gender.asParent()))
			.findFirst().map(td -> getIdFromAnchor(td.select("a").first()));
	}
}
