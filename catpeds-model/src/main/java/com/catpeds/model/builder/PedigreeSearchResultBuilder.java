package com.catpeds.model.builder;

import java.time.LocalDate;

import com.catpeds.model.Pedigree.Gender;
import com.catpeds.model.PedigreeSearchResult;

/**
 * Builder pattern implementation for {@link PedigreeSearchResult}
 *
 * @author padriano
 *
 */
public class PedigreeSearchResultBuilder {

	private PedigreeSearchResult pedigreeSearchResult;

	public PedigreeSearchResultBuilder() {
		this.pedigreeSearchResult = new PedigreeSearchResult();
	}

	public PedigreeSearchResultBuilder withId(long id) {
		pedigreeSearchResult.setId(id);
		return this;
	}

	public PedigreeSearchResultBuilder withName(String name) {
		pedigreeSearchResult.setName(name);
		return this;
	}

	public PedigreeSearchResultBuilder withEms(String ems) {
		pedigreeSearchResult.setEms(ems);
		return this;
	}

	public PedigreeSearchResultBuilder withGender(Gender gender) {
		pedigreeSearchResult.setGender(gender);
		return this;
	}

	public PedigreeSearchResultBuilder withDob(LocalDate dob) {
		pedigreeSearchResult.setDob(dob);
		return this;
	}

	public PedigreeSearchResultBuilder withTitle(String title) {
		pedigreeSearchResult.setTitle(title);
		return this;
	}

	public PedigreeSearchResult build() {
        return pedigreeSearchResult;
    }
}
