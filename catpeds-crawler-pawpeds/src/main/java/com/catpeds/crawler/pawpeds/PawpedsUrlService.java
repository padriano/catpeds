package com.catpeds.crawler.pawpeds;

import static com.google.common.base.Strings.nullToEmpty;

import com.catpeds.model.PedigreeCriteria;

/**
 * Service to provide PawPeds's URLs
 *
 * @author padriano
 *
 */
class PawpedsUrlService {

	// improvement can be done to get the paths from configuration instead
	private static final String URL_ADVANCED_SEARCH = "https://www.pawpeds.com/db/?a=as&p=nfo&date=iso&name=%s&ems=&sex=B&"
			+ "born_after=%s&born_before=%s&born_in=%s&lives_in=%s"
			+ "&picture=B&health_info=B&g=2";

	private static final String URL_OFFSPRING_SEARCH = "https://www.pawpeds.com/db/?a=o&id=%d&g=2&p=nfo&date=iso&o=ajgrep";

	private static final String URL_PEDIGREE = "https://www.pawpeds.com/db/?a=p&v=fi&id=%d&g=1&p=nfo&date=iso&o=ajgrep";

	/**
	 * Get the URL that represents a query on PawPeds for advanced search for
	 * the matching {@link PedigreeCriteria}.
	 * <p>
	 * Be aware that only filtering for date of birth, nationality and location for
	 * NFO's is supported at the moment.
	 * </p>
	 *
	 * @param criteria
	 *            the matcher for the search
	 * @return URL string representation of the query
	 */
	public String getAdvancedSearchUrl(PedigreeCriteria criteria) {

		String bornAfter = criteria.getBornAfter() == null ? "" : criteria.getBornAfter().toString();
		String bornBefore = criteria.getBornBefore() == null ? "" : criteria.getBornBefore().toString();

		return String.format(URL_ADVANCED_SEARCH, nullToEmpty(criteria.getName()), bornAfter, bornBefore,
				nullToEmpty(criteria.getNationalityCountryCode()), nullToEmpty(criteria.getLocationCountryCode()));
	}

	/**
	 * Get the URL that represents a query on PawPeds for an offspring search for
	 * the specified cat.
	 *
	 * @param id
	 *            offspring parent identifier
	 * @return URL string representation of the query
	 */
	public String getOffspringsSearchUrl(long id) {
		return String.format(URL_OFFSPRING_SEARCH, id);
	}

	/**
	 * Get the URL that represents one generation PawPeds pedigree with complete
	 * inbreeding for the specified cat.
	 *
	 * @param id
	 *            cat's pawpeds identifier
	 * @return URL link to pedigree
	 */
	public String getPedigreeUrl(long id) {
		return String.format(URL_PEDIGREE, id);
	}
}
