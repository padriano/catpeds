/**
 *
 */
package com.catpeds.model;

import java.time.LocalDate;

import com.catpeds.model.Pedigree.Gender;

/**
 * POJO representing the pedigree search result.
 *
 * @author padriano
 *
 */
public class PedigreeSearchResult {

	/**
	 * Unique identifier
	 */
	private long id;

	/**
	 * Name
	 */
	private String name;

	/**
	 * FIFe Easy Mind System (EMS) code
	 */
	private String ems;

	/**
	 * Gender
	 */
	private Gender gender;

	/**
	 * Date of birth
	 */
	private LocalDate dob;

	/**
	 * Title
	 */
	private String title;

	/**
	 * @see #id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @see #id
	 */
	public void setId(long id) {
		this.id = id;
	}

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
	 * @see #ems
	 */
	public String getEms() {
		return ems;
	}

	/**
	 * @see #ems
	 */
	public void setEms(String ems) {
		this.ems = ems;
	}

	/**
	 * @see #gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @see #gender
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @see #dob
	 */
	public LocalDate getDob() {
		return dob;
	}

	/**
	 * @see #dob
	 */
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	/**
	 * @see #tile
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @see #tile
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
