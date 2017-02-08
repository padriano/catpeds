package com.catpeds.rest.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.hateoas.Link;

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

	/**
	 * Test that {@link PedigreeResource#equals(Object)} implementation verifies
	 * overridden implementation.
	 */
	@Test
	public void testPedigreeResourceNotEqualsSuper() {
		// Given
		PedigreeResource resourceWithoutLinks = new PedigreeResource(mock(Pedigree.class));
		PedigreeResource resourceWithLinks = new PedigreeResource(mock(Pedigree.class));
		resourceWithLinks.add(mock(Link.class));

		// When && Then
		assertFalse("Resources cannot be equal", resourceWithoutLinks.equals(resourceWithLinks));
	}

	/**
	 * Test that {@link PedigreeResource#equals(Object)} implementation verifies
	 * pedigree field when different.
	 */
	@Test
	public void testPedigreeResourceNonEqualsPedigree() {
		// Given
		PedigreeResource resource = new PedigreeResource(mock(Pedigree.class));
		PedigreeResource otherResource = new PedigreeResource(mock(Pedigree.class));

		// When && Then
		assertFalse("Resources cannot be equal", resource.equals(otherResource));
	}

	/**
	 * Test that {@link PedigreeResource#equals(Object)} implementation verifies
	 * pedigree field when identical.
	 */
	@Test
	public void testPedigreeResourceEqualsPedigree() {
		// Given
		Pedigree pedigree = mock(Pedigree.class);
		PedigreeResource resource = new PedigreeResource(pedigree);
		PedigreeResource otherResource = new PedigreeResource(pedigree);

		// When && Then
		assertTrue("Resources must be equal", resource.equals(otherResource));
	}

	/**
	 * Test that {@link PedigreeResource#hashCode()} implementation verifies
	 * pedigree field when different.
	 */
	@Test
	public void testPedigreeResourceDifferentHashCode() {
		// Given
		PedigreeResource resource = new PedigreeResource(mock(Pedigree.class));
		PedigreeResource otherResource = new PedigreeResource(mock(Pedigree.class));

		// When && Then
		assertNotEquals("Resources should not have same hash code", resource.hashCode(), otherResource.hashCode());
	}

	/**
	 * Test that {@link PedigreeResource#hashCode} implementation verifies
	 * pedigree field when identical.
	 */
	@Test
	public void testPedigreeResourceSameHashCode() {
		// Given
		Pedigree pedigree = mock(Pedigree.class);
		PedigreeResource resource = new PedigreeResource(pedigree);
		PedigreeResource otherResource = new PedigreeResource(pedigree);

		// When && Then
		assertEquals("Resources must have same hash code", resource.hashCode(), otherResource.hashCode());
	}
}
