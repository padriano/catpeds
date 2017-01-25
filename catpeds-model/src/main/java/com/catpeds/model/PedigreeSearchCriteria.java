/**
 *
 */
package com.catpeds.model;

import java.time.LocalDate;

/**
 * POJO representing the fields that usually it is expected that a search can be
 * done on the search engines.
 *
 * @author padriano
 *
 */
public class PedigreeSearchCriteria {

	/**
	 * Name
	 */
	private String name;

	/**
	 * Min date of birth
	 */
	private LocalDate bornAfter;

	/**
	 * Max date of birth
	 */
	private LocalDate bornBefore;

	/**
	 * Nationality country code
	 * <p>ISO 3166-1 alpha-2</p>
	 */
	private String nationalityCountryCode;

	/**
	 * Location country code
	 * <p>ISO 3166-1 alpha-2</p>
	 */
	private String locationCountryCode;

	/**
	 * @see #name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see #name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see #bornAfter
	 */
	public LocalDate getBornAfter() {
		return bornAfter;
	}

	/**
	 * @see #bornAfter
	 */
	public void setBornAfter(LocalDate bornAfter) {
		this.bornAfter = bornAfter;
	}

	/**
	 * @see #bornBefore
	 */
	public LocalDate getBornBefore() {
		return bornBefore;
	}

	/**
	 * @see #bornBefore
	 */
	public void setBornBefore(LocalDate bornBefore) {
		this.bornBefore = bornBefore;
	}

	/**
	 * @see #nationalityCountryCode
	 */
	public String getNationalityCountryCode() {
		return nationalityCountryCode;
	}

	/**
	 * @see #nationalityCountryCode
	 */
	public void setNationalityCountryCode(String nationalityCountryCode) {
		this.nationalityCountryCode = nationalityCountryCode;
	}

	/**
	 * @see #locationCountryCode
	 */
	public String getLocationCountryCode() {
		return locationCountryCode;
	}

	/**
	 * @see #locationCountryCode
	 */
	public void setLocationCountryCode(String locationCountryCode) {
		this.locationCountryCode = locationCountryCode;
	}
}
