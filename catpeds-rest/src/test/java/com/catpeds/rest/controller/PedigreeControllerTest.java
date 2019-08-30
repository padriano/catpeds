package com.catpeds.rest.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.catpeds.crawler.pawpeds.PawpedsPedigreeRepository;
import com.catpeds.model.Pedigree;
import com.catpeds.model.PedigreeCriteria;
import com.catpeds.rest.resource.PedigreeResource;
import com.catpeds.rest.resource.PedigreeResourceFactory;

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

	@MockBean
	private PedigreeResourceFactory pedigreeResourceFactory;

	@MockBean
	private ResponseEntityFactory responseEntityFactory;

	@Inject
	private PedigreeController pedigreeController;

	/**
	 * Test that {@link PedigreeController#findOne(long)} uses the expected
	 * dependencies to get the information and maps it correctly when there is a
	 * result.
	 */
	@Test
	public void testFindOneWithResult() {
		// Given
		long pedigreeId = 123l;
		Pedigree pedigree = mock(Pedigree.class);
		when(pedigree.getId()).thenReturn(pedigreeId);
		when(pawpedsPedigreeRepository.findOne(pedigreeId)).thenReturn(Optional.of(pedigree));
		PedigreeResource pedigreeResource = mock(PedigreeResource.class);
		when(pedigreeResourceFactory.create(pedigree)).thenReturn(pedigreeResource);
		@SuppressWarnings("unchecked")
		ResponseEntity<PedigreeResource> response = mock(ResponseEntity.class);
		when(responseEntityFactory.createOK(pedigreeResource)).thenReturn(response);

		// When
		ResponseEntity<PedigreeResource> result = pedigreeController.findOne(pedigreeId);

		// Then
		assertSame("Expecting the response from the factory for OK", result, response);
	}

	/**
	 * Test that {@link PedigreeController#findOne(long)} uses the expected
	 * dependency to get the information and maps it correctly when there isn't a
	 * result.
	 */
	@Test
	public void testFindOneWithoutResult() {
		// Given
		long pedigreeId = 123l;
		when(pawpedsPedigreeRepository.findOne(pedigreeId)).thenReturn(Optional.empty());
		@SuppressWarnings("unchecked")
		ResponseEntity<Object> response = mock(ResponseEntity.class);
		when(responseEntityFactory.createNotFound()).thenReturn(response);

		// When
		ResponseEntity<PedigreeResource> result = pedigreeController.findOne(pedigreeId);

		// Then
		assertSame("Expecting the response from the factory for NOT FOUND", result, response);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testFind() {
		// Given
		String name = "Foo";
		String nationalityCountryCode = "SE";
		String locationCountryCode = "GB";
		Pedigree pedigree1 = mock(Pedigree.class);
		Pedigree pedigree2 = mock(Pedigree.class);
		when(pawpedsPedigreeRepository.findAll(any(PedigreeCriteria.class)))
				.thenReturn(Arrays.asList(pedigree1, pedigree2));
		PedigreeResource pedigreeResource1 = mock(PedigreeResource.class);
		PedigreeResource pedigreeResource2 = mock(PedigreeResource.class);
		when(pedigreeResourceFactory.create(pedigree1)).thenReturn(pedigreeResource1);
		when(pedigreeResourceFactory.create(pedigree2)).thenReturn(pedigreeResource2);
		ResponseEntity<Object> response = mock(ResponseEntity.class);
		when(responseEntityFactory.createOK(any())).thenReturn(response);

		// When
		ResponseEntity<Collection<PedigreeResource>> result = pedigreeController.find(name, nationalityCountryCode,
				locationCountryCode);

		// Then
		assertSame("Expecting the response from the factory", result, response);
		ArgumentCaptor<PedigreeCriteria> pedigreeCriteriaArgumentCaptor = ArgumentCaptor
				.forClass(PedigreeCriteria.class);
		verify(pawpedsPedigreeRepository).findAll(pedigreeCriteriaArgumentCaptor.capture());
		PedigreeCriteria pedigreeCriteria = pedigreeCriteriaArgumentCaptor.getValue();
		assertEquals("Unexpected criteria name property", name, pedigreeCriteria.getName());
		assertEquals("Unexpected criteria nationalityCountryCode property", nationalityCountryCode,
				pedigreeCriteria.getNationalityCountryCode());
		assertEquals("Unexpected criteria locationCountryCode property", locationCountryCode,
				pedigreeCriteria.getLocationCountryCode());

		ArgumentCaptor<List<PedigreeResource>> resourcesArgumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(responseEntityFactory).createOK(resourcesArgumentCaptor.capture());
		List<PedigreeResource> pedigreeResources = resourcesArgumentCaptor.getValue();
		assertEquals("Expecting 2 pedigree's", 2, pedigreeResources.size());
		assertEquals("Expecting pedigree resource 1", pedigreeResource1, pedigreeResources.get(0));
		assertEquals("Expecting pedigree resource 2", pedigreeResource2, pedigreeResources.get(1));
	}


	/**
	 * Test that {@link PedigreeController#escapeParamFunc} returns null for empty parameter value.
	 */
	@Test
	public void testEmptyParameterEscapeFunction() {
		// Given
		String param = "";

		// When
		String result = PedigreeController.escapeParamFunc.apply(param);

		// Then
		assertNull("Expecting null response", result);
	}


	/**
	 * Test that {@link PedigreeController#escapeParamFunc} escapes parameter.
	 */
	@Test
	public void testParameterEscapeFunction() {
		// Given
		String param = "A\nB\rC\tD";

		// When
		String result = PedigreeController.escapeParamFunc.apply(param);

		// Then
		assertEquals("Expecting escaped response", result, "A_B_C_D");
	}
}
