/**
 *
 */
package com.catpeds.crawler.pawpeds;

import java.util.Collection;

import com.catpeds.model.PedigreeSearchCriteria;
import com.catpeds.model.PedigreeSearchResult;

/**
 * Repository to retrieve pedigree information from PawPeds.
 *
 * @author padriano
 *
 */
@FunctionalInterface
public interface PawpedsRepository {

	/**
	 * Returns a collection of pedigree with the available fields.
	 *
	 * @param criteria - cannot be null
	 * @return a {@link Collection} of {@link PedigreeSearchResult} matching the given criteria.
	 */
	Collection<PedigreeSearchResult> findAll(PedigreeSearchCriteria criteria);
}
