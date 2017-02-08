package com.catpeds.rest.resource;

import java.util.Objects;

import org.springframework.hateoas.ResourceSupport;

import com.catpeds.model.Pedigree;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@link Pedigree}'s {@link ResourceSupport}
 *
 * @author padriano
 *
 */
public class PedigreeResource extends ResourceSupport {

	/**
	 * Pedigree information content
	 */
	private final Pedigree pedigree;

	@JsonCreator
	public PedigreeResource(@JsonProperty("pedigree") Pedigree pedigree) {
		this.pedigree = pedigree;
	}

	/**
	 * @see #pedigree
	 *
	 * @return the pedigree
	 */
	public Pedigree getPedigree() {
		return pedigree;
	}

	/**
	 * @see org.springframework.hateoas.ResourceSupport#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		if (Objects.equals(pedigree, ((PedigreeResource) obj).getPedigree())) {
			return true;
		}
		return false;
	}

	/**
	 * @see org.springframework.hateoas.ResourceSupport#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), pedigree);
	}
}
