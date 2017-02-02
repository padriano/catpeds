package com.catpeds.crawler.pawpeds;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.catpeds.model.PedigreeCriteria;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Unit test class for {@link PawpedsUrlService}
 *
 * @author padriano
 *
 */
@RunWith(JUnitParamsRunner.class)
public class PawpedsUrlServiceTest {

	private PawpedsUrlService pawpedsUrlService;

	/**
	 * Initialisation
	 */
	@Before
	public void setup() {
		pawpedsUrlService = new PawpedsUrlService();
	}

	/**
	 * Test that
	 * {@link PawpedsUrlService#getAdvancedSearchUrl(PedigreeCriteria)}
	 * creates the correct URL filtering the DoB, nationality and location.
	 */
	@Test
	public void testGetAdvancedSearchUrlAllProperties() {
		// Given
		PedigreeCriteria criteria = new PedigreeCriteria();
		criteria.setBornAfter(LocalDate.of(1980, 1, 1));
		criteria.setBornBefore(LocalDate.of(1981, 12, 31));
		criteria.setNationalityCountryCode("SE");
		criteria.setLocationCountryCode("GB");
		criteria.setName("Lothiriel de Rivendell's Land");

		// When
		String url = pawpedsUrlService.getAdvancedSearchUrl(criteria);

		// Then
		String expectedUrl = "https://www.pawpeds.com/db/?a=as&p=nfo&date=iso&"
				+ "name=Lothiriel%20de%20Rivendell's%20Land&ems=&sex=B&"
				+ "born_after=1980-01-01&born_before=1981-12-31&born_in=SE&lives_in=GB"
				+ "&picture=B&health_info=B&g=2";
		assertEquals(expectedUrl, url);
	}

	Object[] countryCodesParams() {
		return new Object[][] {
			{"", ""},
			{"", null},
			{null, ""},
			{null, null},
		};
	}

	/**
	 * Test that
	 * {@link PawpedsUrlService#getAdvancedSearchUrl(PedigreeCriteria)}
	 * creates the correct URL filtering the DoB, nationality and location.
	 * @param nationalityCountryCode
	 * @param locationCountryCode
	 */
	@Test
	@Parameters(method = "countryCodesParams")
	public void testGetAdvancedSearchUrlForCountryCodes(String nationalityCountryCode, String locationCountryCode) {
		// Given
		PedigreeCriteria criteria = new PedigreeCriteria();
		criteria.setNationalityCountryCode(nationalityCountryCode);
		criteria.setLocationCountryCode(locationCountryCode);

		// When
		String url = pawpedsUrlService.getAdvancedSearchUrl(criteria);

		// Then
		String expectedUrl = "https://www.pawpeds.com/db/?a=as&p=nfo&date=iso&name=&ems=&sex=B&"
				+ "born_after=&born_before=&born_in=&lives_in="
				+ "&picture=B&health_info=B&g=2";
		assertEquals(expectedUrl, url);
	}

	/**
	 * Test that
	 * {@link PawpedsUrlService#getOffspringsSearchUrl(long)}
	 * creates the correct URL filtering using the parent's id.
	 */
	@Test
	public void testGetOffspringsSearchUrl() {
		// Given
		long parentId = 1212;

		// When
		String url = pawpedsUrlService.getOffspringsSearchUrl(parentId);

		// Then
		String expectedUrl = "https://www.pawpeds.com/db/?a=o&id=" + parentId + "&g=2&p=nfo&date=iso&o=ajgrep";
		assertEquals(expectedUrl, url);
	}

	/**
	 * Test that {@link PawpedsUrlService#getPedigreeUrl(long)} creates the
	 * correct URL for the cat's pedigree.
	 */
	@Test
	public void testGetPedigreeUrl() {
		// Given
		long id = 1212;

		// When
		String url = pawpedsUrlService.getPedigreeUrl(id);

		// Then
		String expectedUrl = "https://www.pawpeds.com/db/?a=p&v=fi&id=" + id + "&g=1&p=nfo&date=iso&o=ajgrep";
		assertEquals(expectedUrl, url);
	}
}
