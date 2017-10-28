package com.catpeds.rest.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.hateoas.Link;

import com.catpeds.model.Pedigree;
import com.catpeds.rest.resource.PedigreeResourceFactory.HateoasFactory;

/**
 * Unit test class for {@link PedigreeResourceFactory}
 *
 * @author padriano
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PedigreeResourceFactoryTest {

	@Mock
	private HateoasFactory hateoasFactory;

	@InjectMocks
	private PedigreeResourceFactory pedigreeResourceFactory;

	@Mock
	private Pedigree pedigree;

	/**
	 * Check that {@link PedigreeResourceFactory#create(Pedigree)} instantiates the
	 * {@link PedigreeResource} with the {@link Pedigree} and Link.
	 */
	@Test
	public void testCreate() {
		// Given
		Link link = mock(Link.class);
		when(hateoasFactory.createResourceLink(pedigree)).thenReturn(link);

		// When
		PedigreeResource resource = pedigreeResourceFactory.create(pedigree);

		// Then
		assertNotNull("Expecting an instance", resource);
		assertSame("Expecting the pedigree passed as argument", pedigree, resource.getPedigree());
		assertEquals("Expecting one link", 1, resource.getLinks().size());
		assertSame("Expecting the Link instance for the Link factory", link, resource.getLinks().get(0));
	}
}
