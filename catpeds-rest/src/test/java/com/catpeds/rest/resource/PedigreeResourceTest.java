package com.catpeds.rest.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.catpeds.model.Pedigree;

/**
 * Unit test class for {@link PedigreeResource}
 *
 * @author padriano
 *
 */
public class PedigreeResourceTest {

	/**
	 * Test that {@link PedigreeResource} is correctly instantiated with the
	 * expected pedigree information.
	 */
	@Test
	public void testPedigreeResourceInstantiation() {
		// Given
		Pedigree pedigree = mock(Pedigree.class);
		PedigreeResource pedigreeResource = new PedigreeResource(pedigree);

		// When
		Pedigree result = pedigreeResource.getPedigree();

		// Then
		assertEquals("Expecting the pedigree passed in on resource instantiation", pedigree, result);
	}
}
