/**
 *
 */
package com.catpeds.crawler.pawpeds;

import java.util.Collection;
import java.util.Optional;

import com.catpeds.model.Pedigree;
import com.catpeds.model.PedigreeCriteria;

import javax.annotation.Untainted;

/**
 * Repository to retrieve pedigree information from PawPeds.
 *
 * @author padriano
 *
 */
public interface PawpedsPedigreeRepository {

	/**
	 * Returns a collection of pedigree with the available fields.
	 * <p>
	 * Be aware that only filtering for name, date of birth, nationality and
	 * location for NFO's is supported at the moment.
	 * </p>
	 *
	 * @param criteria
	 *            - cannot be null
	 * @return a {@link Collection} of {@link Pedigree} matching the given
	 *         criteria with the title, name, gender, EMS and date of birth
	 *         populated fields.
	 */
	Collection<Pedigree> findAll(@Untainted PedigreeCriteria criteria);

	/**
	 * Returns a collection of pedigree for the specified cat's offsprings.
	 *
	 * @param id
	 *            - Parent's pedigree unique identifier
	 * @return a {@link Collection} of {@link Pedigree} with the title, name,
	 *         gender, EMS and date of birth populated fields.
	 */
	Collection<Pedigree> findAllOffspring(long id);

	/**
	 * Retrieves a PawPeds pedigree by its id.
	 * <p>
	 * Due to limitations on the PawPeds HTML pedigree page the title and the
	 * name are displayed concatenated, so the title will also be in the same
	 * format in the returned information with a <code>null</code> title. On the
	 * {@link PedigreeSearchResult} the title and name are separate so
	 * {@link #findAll(PedigreeCriteria)} can be used to extract this title/name
	 * split more accurately.
	 * </p>
	 *
	 * @param id
	 *            cat unique identifier
	 *
	 * @return An {@link Optional} with the {@link Pedigree} instance or empty.
	 */
	Optional<Pedigree> findOne(long id);
}
