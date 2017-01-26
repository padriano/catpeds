package com.catpeds.model.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Test;

import com.catpeds.model.Pedigree.Gender;
import com.catpeds.model.PedigreeSearchResult;

/**
 * Unit test class for {@link PedigreeSearchResultBuilder}
 *
 * @author padriano
 *
 */
public class PedigreeSearchResultBuilderTest {

	/**
	 * Test that {@link PedigreeSearchResultBuilder} instantiates a
	 * {@link PedigreeSearchResult} with all it's properties correctly
	 * populated.
	 */
	@Test
	public void testBuilder() {
		// Given
		PedigreeSearchResultBuilder builder = new PedigreeSearchResultBuilder();

		long id = 123l;
		String name = "Foo";
		String title = "IC";
		String ems = "NFO a 03";
		Gender gender = Gender.M;
		LocalDate dob = LocalDate.of(2015, 1, 1);

		// When
		PedigreeSearchResult pedigreeSearchResult = builder.withId(id).withName(name).withTitle(title).withEms(ems)
				.withGender(gender).withDob(dob).build();

		// Then
		assertNotNull(pedigreeSearchResult);
		assertEquals(id, pedigreeSearchResult.getId());
		assertEquals(name, pedigreeSearchResult.getName());
		assertEquals(title, pedigreeSearchResult.getTitle());
		assertEquals(ems, pedigreeSearchResult.getEms());
		assertEquals(gender, pedigreeSearchResult.getGender());
		assertEquals(dob, pedigreeSearchResult.getDob());
	}
}
