package com.catpeds.model.comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.catpeds.model.Pedigree.Gender;
import com.catpeds.model.PedigreeSearchResult;
import com.catpeds.model.builder.PedigreeSearchResultBuilder;

/**
 * Unit test class for {@link PedigreeSearchResultComparator}
 *
 * @author padriano
 *
 */
public class PedigreeSearchResultComparatorTest {

	private PedigreeSearchResult corePed;

	private PedigreeSearchResultComparator comparator;

	/**
	 * Initialisation
	 */
	@Before
	public void setup() {
		comparator = new PedigreeSearchResultComparator();
		corePed = new PedigreeSearchResultBuilder().withId(123l).withName("Foo").withTitle("IC")
				.withEms("NFO a 03").withGender(Gender.M).withDob(LocalDate.of(2015, 1, 1)).build();
	}

	/**
	 * Test that {@link PedigreeSearchResultComparator} asserts equality in
	 * identical {@link PedigreeSearchResult}s.
	 */
	@Test
	public void testEquality() {
		// Given
		PedigreeSearchResult otherPed = new PedigreeSearchResultBuilder().withId(123l).withName("Foo").withTitle("IC")
				.withEms("NFO a 03").withGender(Gender.M).withDob(LocalDate.of(2015, 1, 1)).build();

		// When
		int result = comparator.compare(corePed, otherPed);

		// Then
		assertEquals("Expecting equality result", 0, result);
	}

	/**
	 * Test that {@link PedigreeSearchResultComparator} asserts non equality in
	 * {@link PedigreeSearchResult}s with different id.
	 */
	@Test
	public void testNonIdEquality() {
		// Given
		PedigreeSearchResult otherPed = new PedigreeSearchResultBuilder().withId(321l).withName("Foo").withTitle("IC")
				.withEms("NFO a 03").withGender(Gender.M).withDob(LocalDate.of(2015, 1, 1)).build();

		// When
		int result = comparator.compare(corePed, otherPed);

		// Then
		assertNotEquals("Not expecting equality result", 0, result);
	}

	/**
	 * Test that {@link PedigreeSearchResultComparator} asserts non equality in
	 * {@link PedigreeSearchResult}s with different name.
	 */
	@Test
	public void testNonNameEquality() {
		// Given
		PedigreeSearchResult otherPed = new PedigreeSearchResultBuilder().withId(123l).withName("Bar").withTitle("IC")
				.withEms("NFO a 03").withGender(Gender.M).withDob(LocalDate.of(2015, 1, 1)).build();

		// When
		int result = comparator.compare(corePed, otherPed);

		// Then
		assertNotEquals("Not expecting equality result", 0, result);
	}

	/**
	 * Test that {@link PedigreeSearchResultComparator} asserts non equality in
	 * {@link PedigreeSearchResult}s with different title.
	 */
	@Test
	public void testNonTitleEquality() {
		// Given
		PedigreeSearchResult otherPed = new PedigreeSearchResultBuilder().withId(123l).withName("Foo").withTitle("CH")
				.withEms("NFO a 03").withGender(Gender.M).withDob(LocalDate.of(2015, 1, 1)).build();

		// When
		int result = comparator.compare(corePed, otherPed);

		// Then
		assertNotEquals("Not expecting equality result", 0, result);
	}

	/**
	 * Test that {@link PedigreeSearchResultComparator} asserts non equality in
	 * {@link PedigreeSearchResult}s with different EMS.
	 */
	@Test
	public void testNonEmsEquality() {
		// Given
		PedigreeSearchResult otherPed = new PedigreeSearchResultBuilder().withId(123l).withName("Foo").withTitle("IC")
				.withEms("NFO a 09").withGender(Gender.M).withDob(LocalDate.of(2015, 1, 1)).build();

		// When
		int result = comparator.compare(corePed, otherPed);

		// Then
		assertNotEquals("Not expecting equality result", 0, result);
	}

	/**
	 * Test that {@link PedigreeSearchResultComparator} asserts non equality in
	 * {@link PedigreeSearchResult}s with different gender.
	 */
	@Test
	public void testNonGenderEquality() {
		// Given
		PedigreeSearchResult otherPed = new PedigreeSearchResultBuilder().withId(123l).withName("Foo").withTitle("IC")
				.withEms("NFO a 03").withGender(Gender.F).withDob(LocalDate.of(2015, 1, 1)).build();

		// When
		int result = comparator.compare(corePed, otherPed);

		// Then
		assertNotEquals("Not expecting equality result", 0, result);
	}

	/**
	 * Test that {@link PedigreeSearchResultComparator} asserts non equality in
	 * {@link PedigreeSearchResult}s with different DoB.
	 */
	@Test
	public void testNonDobEquality() {
		// Given
		PedigreeSearchResult otherPed = new PedigreeSearchResultBuilder().withId(123l).withName("Foo").withTitle("IC")
				.withEms("NFO a 03").withGender(Gender.M).withDob(LocalDate.of(2016, 1, 1)).build();

		// When
		int result = comparator.compare(corePed, otherPed);

		// Then
		assertNotEquals("Not expecting equality result", 0, result);
	}
}
