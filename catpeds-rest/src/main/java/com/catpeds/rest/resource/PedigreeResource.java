package com.catpeds.rest.resource;

import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.catpeds.model.Pedigree;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@link Pedigree}'s {@link RepresentationModel}
 *
 * @author padriano
 *
 */
public class PedigreeResource extends RepresentationModel {

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
	 * @see org.springframework.hateoas.RepresentationModel#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		return Objects.equals(pedigree, ((PedigreeResource) obj).getPedigree());
	}

	/**
	 * @see org.springframework.hateoas.RepresentationModel#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), pedigree);
	}
}
