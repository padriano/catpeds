package com.catpeds.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import com.catpeds.model.Pedigree.Gender;

/**
 * Unit test class for {@link Pedigree}
 *
 * @author padriano
 *
 */
public class PedigreeTest {

	/**
	 * Initialise
	 */
	@Test
	public void testToString() {
		// Given
		Pedigree pedigree = new Pedigree();
		pedigree.setId(123);
		pedigree.setDamId(11l);
		pedigree.setSireId(22l);
		pedigree.setDob(LocalDate.of(2017, 1, 31));
		pedigree.setEms("NFO w");
		pedigree.setGender(Gender.M);
		pedigree.setInbreeding(12.3);
		pedigree.setLocationCountryCode("GB");
		pedigree.setNationalityCountryCode("SE");
		pedigree.setName("Padawan Amidala");
		pedigree.setTitle("CH");

		// When
		String result = pedigree.toString();

		// Then
		String expectedResult = "Pedigree{id=123, name=Padawan Amidala, ems=NFO w, gender=M, dob=2017-01-31, "
				+ "nationalityCountryCode=SE, locationCountryCode=GB, title=CH, inbreeding=12.3, sire=22, dam=11}";
		assertEquals(expectedResult, result);
	}
}
