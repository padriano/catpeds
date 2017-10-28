package com.catpeds.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit test class for {@link ResponseEntityFactory}
 *
 * @author padriano
 *
 */
public class ResponseEntityFactoryTest {

	private ResponseEntityFactory responseEntityFactory;

	@Before
	public void setup() {
		responseEntityFactory = new ResponseEntityFactory();
	}

	/**
	 * Check that {@link ResponseEntityFactory#createOK(Object)} instantiates a
	 * {@link ResponseEntity} with {@link HttpStatus#OK} and the specified body.
	 */
	@Test
	public void testCreateOK() {
		// Given
		Object object = new Object();

		// When
		ResponseEntity<Object> responseEntity = responseEntityFactory.createOK(object);

		// Then
		assertNotNull("Expecting an instance", responseEntity);
		assertEquals("Unpexpected HTTP status code", HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Unpexpected body", object, responseEntity.getBody());
	}

	/**
	 * Check that {@link ResponseEntityFactory#createNotFound()} instantiates a
	 * {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND} and no body.
	 */
	@Test
	public void testCreateNotFound() {
		// Given

		// When
		ResponseEntity<Object> responseEntity = responseEntityFactory.createNotFound();

		// Then
		assertNotNull("Expecting an instance", responseEntity);
		assertEquals("Unpexpected HTTP status code", HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull("Unpexpected body", responseEntity.getBody());
	}
}
