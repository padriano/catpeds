package com.catpeds.model.builder;

import java.time.LocalDate;

import com.catpeds.model.Pedigree;
import com.catpeds.model.Pedigree.Gender;

/**
 * Builder pattern implementation for {@link Pedigree}
 *
 * @author padriano
 *
 */
public class PedigreeBuilder {

	private Pedigree pedigree;

	public PedigreeBuilder() {
		pedigree = new Pedigree();
	}

	public PedigreeBuilder withId(long id) {
		pedigree.setId(id);
		return this;
	}

	public PedigreeBuilder withName(String name) {
		pedigree.setName(name);
		return this;
	}

	public PedigreeBuilder withEms(String ems) {
		pedigree.setEms(ems);
		return this;
	}

	public PedigreeBuilder withGender(Gender gender) {
		pedigree.setGender(gender);
		return this;
	}

	public PedigreeBuilder withDob(LocalDate dob) {
		pedigree.setDob(dob);
		return this;
	}

	public PedigreeBuilder withNationalityCountryCode(String nationalityCountryCode) {
		pedigree.setNationalityCountryCode(nationalityCountryCode);
		return this;
	}

	public PedigreeBuilder withLocationCountryCode(String locationCountryCode) {
		pedigree.setLocationCountryCode(locationCountryCode);
		return this;
	}

	public PedigreeBuilder withTitle(String title) {
		pedigree.setTitle(title);
		return this;
	}

	public PedigreeBuilder withInbreeding(double inbreeding) {
		pedigree.setInbreeding(inbreeding);
		return this;
	}

	public PedigreeBuilder withSireId(Long sireId) {
		pedigree.setSireId(sireId);
		return this;
	}

	public PedigreeBuilder withDamId(Long damId) {
		pedigree.setDamId(damId);
		return this;
	}

	public PedigreeBuilder withOffspring(Long... offsprings) {
		pedigree.addOffspring(offsprings);
		return this;
	}

	public Pedigree build() {
		return pedigree;
	}
}
