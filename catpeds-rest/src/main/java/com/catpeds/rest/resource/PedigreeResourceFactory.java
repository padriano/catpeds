package com.catpeds.rest.resource;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.catpeds.model.Pedigree;
import com.catpeds.rest.controller.PedigreeController;

/**
 * Factory to instantiate {@link PedigreeResource} from {@link Pedigree}
 *
 * @author padriano
 *
 */
@Service
public class PedigreeResourceFactory {

	private final HateoasFactory hateoasFactory;

	@Inject
	PedigreeResourceFactory(HateoasFactory hateoasFactory) {
		this.hateoasFactory = hateoasFactory;
	}

	/**
	 * Create {@link PedigreeResource} for the {@link Pedigree} with a link to his
	 * controller method that returns his information.
	 *
	 * @param pedigree
	 *            Resource information
	 * @return the instantiated {@link PedigreeResource} instance
	 */
	public PedigreeResource create(Pedigree pedigree) {
		PedigreeResource pedigreeResource = new PedigreeResource(pedigree);
		pedigreeResource.add(hateoasFactory.createResourceLink(pedigree));
		return pedigreeResource;
	}

	@Component
	static class HateoasFactory {

		Link createResourceLink(Pedigree pedigree) {
			return linkTo(methodOn(PedigreeController.class).findOne(pedigree.getId())).withSelfRel();
		}
	}
}
