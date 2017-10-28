package com.catpeds.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Factory to instantiate {@link ResponseEntity} objects for a specific
 * {@link HttpStatus} code.
 *
 * @author padriano
 *
 */
@Service
class ResponseEntityFactory {

	/**
	 * Create {@link ResponseEntity} with specified body and {@link HttpStatus#OK}
	 *
	 * @param object
	 *            Response body
	 * @return {@link ResponseEntity} instance
	 */
	<T> ResponseEntity<T> createOK(T object) {
		return new ResponseEntity<>(object, HttpStatus.OK);
	}

	/**
	 * Create {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND} and no body.
	 *
	 * @return {@link ResponseEntity} instance
	 */
	<T> ResponseEntity<T> createNotFound() {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
