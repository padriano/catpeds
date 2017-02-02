package com.catpeds.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Unit test class for {@link PedigreeCriteria}
 *
 * @author padriano
 *
 */
public class PedigreeCriteriaTest {

	/**
	 * Test {@link PedigreeCriteria} string representation
	 */
	@Test
	public void testToString() {
		// Given
		PedigreeCriteria pedigreeCriteria= new PedigreeCriteria();
		pedigreeCriteria.setBornAfter(LocalDate.of(2017, 1, 1));
		pedigreeCriteria.setBornBefore(LocalDate.of(2017, 12, 31));
		pedigreeCriteria.setLocationCountryCode("GB");
		pedigreeCriteria.setNationalityCountryCode("SE");
		pedigreeCriteria.setName("Padawan Amidala");

		// When
		String result = pedigreeCriteria.toString();

		// Then
		String expectedResult = "PedigreeCriteria{name=Padawan Amidala, "
				+ "bornAfter=2017-01-01, bornBefore=2017-12-31, nationalityCountryCode=SE, "
				+ "locationCountryCode=GB}";
		assertEquals(expectedResult, result);
	}
}
