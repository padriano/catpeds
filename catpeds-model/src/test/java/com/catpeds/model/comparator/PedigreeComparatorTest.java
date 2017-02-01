package com.catpeds.model.comparator;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.catpeds.model.Pedigree;
import com.catpeds.model.Pedigree.Gender;
import com.catpeds.model.builder.PedigreeBuilder;

/**
 * Unit test class for {@link PedigreeComparator}
 *
 * @author padriano
 *
 */
public class PedigreeComparatorTest {

	private PedigreeComparator comparator;
	private Pedigree pedigree;
	private Pedigree otherIdenticalPedigree;

	/**
	 * Initialisation
	 */
	@Before
	public void setup() {
		comparator = new PedigreeComparator();

		pedigree = new PedigreeBuilder().withId(123).withDamId(123l).withSireId(321l).withDob(LocalDate.of(2015, 1, 1))
				.withEms("NFO a").withGender(Gender.M).withInbreeding(2.2).withLocationCountryCode("GB")
				.withName("Lothiriel").withNationalityCountryCode("PT").withTitle("CH")
				.withOffspring(456345l, 7467456l, 45567l).build();
		otherIdenticalPedigree = new PedigreeBuilder().withId(123).withDamId(123l).withSireId(321l)
				.withDob(LocalDate.of(2015, 1, 1)).withEms("NFO a").withGender(Gender.M).withInbreeding(2.2)
				.withLocationCountryCode("GB").withName("Lothiriel").withNationalityCountryCode("PT").withTitle("CH")
				.withOffspring(456345l, 7467456l, 45567l).build();
	}

	/**
	 * Test identical pedigree's
	 */
	@Test
	public void testComparatorIdenticalPadigrees() {
		// Given

		// When
		int result = comparator.compare(pedigree, otherIdenticalPedigree);

		// Then
		assertEquals("Expecting identical pedigree", 0, result);
	}

	/**
	 * Test different pedigree's haveing more offsprings
	 */
	@Test
	public void testComparatorPadigreesWithMoreOffsprings() {
		// Given
		otherIdenticalPedigree.addOffspring(98765l);

		// When
		int result = comparator.compare(pedigree, otherIdenticalPedigree);

		// Then
		assertEquals("Expecting different pedigree", -1, result);
	}

	/**
	 * Test different pedigree's
	 */
	@Test
	public void testComparatorPadigreesWithDifferentOffsprings() {
		// Given
		Pedigree yetAnotherIdenticalPedigree = new PedigreeBuilder().withId(123).withDamId(123l).withSireId(321l)
				.withDob(LocalDate.of(2015, 1, 1)).withEms("NFO a").withGender(Gender.M).withInbreeding(2.2)
				.withLocationCountryCode("GB").withName("Lothiriel").withNationalityCountryCode("PT").withTitle("CH")
				.withOffspring(456345l, 7467456l, 444444l).build();

		// When
		int result = comparator.compare(pedigree, yetAnotherIdenticalPedigree);

		// Then
		assertEquals("Expecting different pedigree", -1, result);
	}
}
