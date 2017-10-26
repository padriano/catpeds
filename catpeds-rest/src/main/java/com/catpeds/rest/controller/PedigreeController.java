package com.catpeds.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.catpeds.crawler.pawpeds.PawpedsPedigreeRepository;
import com.catpeds.model.Pedigree;
import com.catpeds.rest.resource.PedigreeResource;

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

	private final HateoasFactory hateoasFactory;

	@Inject
	PedigreeController(PawpedsPedigreeRepository pawpedsPedigreeRepository, HateoasFactory hateoasFactory) {
		this.pawpedsPedigreeRepository = pawpedsPedigreeRepository;
		this.hateoasFactory = hateoasFactory;
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
	@GetMapping("/pedigree/{id}")
	public ResponseEntity<PedigreeResource> findOne(@PathVariable long id) {
		LOGGER.info("findOne with id {}", id);

		return pawpedsPedigreeRepository
				.findOne(id)
				.map(this::mapToFound)
				.orElseGet(this::mapToNotFound);
    }

	ResponseEntity<PedigreeResource> mapToFound(Pedigree pedigree) {
		PedigreeResource pedigreeResource = new PedigreeResource(pedigree);
        pedigreeResource.add(hateoasFactory.createResourceLink(pedigree));
        return new ResponseEntity<>(pedigreeResource, HttpStatus.OK);
	}

	ResponseEntity<PedigreeResource> mapToNotFound() {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Service
	static class HateoasFactory {

		Link createResourceLink(Pedigree pedigree) {
			return linkTo(methodOn(PedigreeController.class).findOne(pedigree.getId())).withSelfRel();
		}
	}
}
