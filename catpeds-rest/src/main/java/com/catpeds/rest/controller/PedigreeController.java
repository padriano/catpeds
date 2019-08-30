package com.catpeds.rest.controller;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.function.Function;

import javax.inject.Inject;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catpeds.aop.Track;
import com.catpeds.crawler.pawpeds.PawpedsPedigreeRepository;
import com.catpeds.model.Pedigree;
import com.catpeds.model.PedigreeCriteria;
import com.catpeds.rest.resource.PedigreeResource;
import com.catpeds.rest.resource.PedigreeResourceFactory;

/**
 * {@link Pedigree} information REST controller endpoint.
 *
 * @author padriano
 *
 */
@RestController
public class PedigreeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PedigreeController.class);

	private final PawpedsPedigreeRepository pawpedsPedigreeRepository;

	private final PedigreeResourceFactory pedigreeResourceFactory;

	private final ResponseEntityFactory responseEntityFactory;

	static final Function<String, String> escapeParamFunc = s -> Strings.isNullOrEmpty(s) ? null : s.replaceAll("[\n|\r|\t]", "_");

	@Inject
	PedigreeController(PawpedsPedigreeRepository pawpedsPedigreeRepository,
			PedigreeResourceFactory pedigreeResourceFactory, ResponseEntityFactory responseEntityFactory) {
		this.pawpedsPedigreeRepository = pawpedsPedigreeRepository;
		this.pedigreeResourceFactory = pedigreeResourceFactory;
		this.responseEntityFactory = responseEntityFactory;
	}

	/**
	 * Find the pedigree information for the identified cat.
	 *
	 * @param id
	 *            cat's pedigree unique identifier
	 * @return {@link ResponseEntity} instance for the {@link PedigreeResource} and
	 *         {@link HttpStatus#OK} when found or {@link HttpStatus#NOT_FOUND}
	 *         otherwise
	 */
	@Track
	@GetMapping("/pedigree/{id}")
	public ResponseEntity<PedigreeResource> findOne(@PathVariable long id) {
		LOGGER.info("findOne with id {}", id);

		return pawpedsPedigreeRepository
				.findOne(id)
				.map(pedigreeResourceFactory::create)
				.map(responseEntityFactory::createOK)
				.orElseGet(responseEntityFactory::createNotFound);
    }

	/**
	 * Find the pedigree's information for the searched cats.
	 *
	 * @param name
	 *            cat's pedigree name
	 * @param nationalityCountryCode
	 *            country code where the cat is living
	 * @param locationCountryCode
	 *            country code where the cat was born
	 *
	 * @return {@link ResponseEntity} instance for the {@link Collection} of
	 *         {@link PedigreeResource}'s
	 */
	@Track
	@GetMapping("/pedigree")
	public ResponseEntity<Collection<PedigreeResource>> find(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "nationalityCountryCode", required = false) String nationalityCountryCode,
			@RequestParam(value = "locationCountryCode", required = false) String locationCountryCode) {
		LOGGER.info("find with name {} nationalityCountryCode {} locationCountryCode {}",
				escapeParamFunc.apply(name), escapeParamFunc.apply(nationalityCountryCode),
				escapeParamFunc.apply(locationCountryCode));

		PedigreeCriteria criteria = new PedigreeCriteria();
		criteria.setName(name);
		criteria.setNationalityCountryCode(nationalityCountryCode);
		criteria.setLocationCountryCode(locationCountryCode);

		return pawpedsPedigreeRepository.findAll(criteria).stream().map(pedigreeResourceFactory::create)
				.collect(collectingAndThen(toList(), responseEntityFactory::createOK));
	}

}
