package com.catpeds.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.catpeds.crawler.pawpeds.PawpedsPedigreeRepository;
import com.catpeds.model.Pedigree;
import com.catpeds.rest.resource.PedigreeResource;

/**
 * Unit test class for {@link PedigreeController}
 *
 * @author padriano
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PedigreeControllerTest {

	@MockBean
	private PawpedsPedigreeRepository pawpedsPedigreeRepository;

	@Inject
	private PedigreeController pedigreeController;

	/**
	 * Test that {@link PedigreeController#findOne(long)} uses the expected
	 * dependency to get the information and maps it correctly when there is a
	 * result.
	 */
	@Test
	public void testFindOneWithResult() {
		// Given
		long pedigreeId = 123l;
		Pedigree pedigree = mock(Pedigree.class);
		when(pedigree.getId()).thenReturn(pedigreeId);
		when(pawpedsPedigreeRepository.findOne(pedigreeId)).thenReturn(Optional.of(pedigree));

		// When
		ResponseEntity<PedigreeResource> result = pedigreeController.findOne(pedigreeId);

		// Then
		assertEquals("Expecting an OK status code", HttpStatus.OK, result.getStatusCode());
		assertEquals("Expecting the pedigree returned by the repository", pedigree, result.getBody().getPedigree());
		assertEquals("Expecting just a link", 1, result.getBody().getLinks().size());
		assertEquals("Expecting a link to the pedigree",
				linkTo(methodOn(PedigreeController.class).findOne(pedigreeId)).withSelfRel(),
				result.getBody().getLinks().get(0));
	}

	/**
	 * Test that {@link PedigreeController#findOne(long)} uses the expected
	 * dependency to get the information and maps it correctly when there isn't
	 * a result.
	 */
	@Test
	public void testFindOneWithoutResult() {
		// Given
		long pedigreeId = 123l;
		when(pawpedsPedigreeRepository.findOne(pedigreeId)).thenReturn(Optional.empty());

		// When
		ResponseEntity<PedigreeResource> result = pedigreeController.findOne(pedigreeId);

		// Then
		assertEquals("Expecting an NOT FOUND status code", HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull("Not expecting a pedigree", result.getBody());
	}
}
