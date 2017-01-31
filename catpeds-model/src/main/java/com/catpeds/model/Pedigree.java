package com.catpeds.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.MoreObjects;

/**
 * POJO representing the information provided in a Pedigree document.
 *
 * @author padriano
 *
 */
public class Pedigree {

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
	 * Title
	 */
	private String title;

	/**
	 * Coefficient of inbreeding
	 */
	private double inbreeding;

	/**
	 * Sire unique identifier
	 */
	private long sireId;

	/**
	 * Dam unique identifier
	 */
	private long damId;

	/**
	 * Set of offsprings
	 */
	private Set<Long> offsprings = new HashSet<>();


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

	/**
	 * @see #inbreeding
	 */
	public double getInbreeding() {
		return inbreeding;
	}

	/**
	 * @see #inbreeding
	 */
	public void setInbreeding(double inbreeding) {
		this.inbreeding = inbreeding;
	}

	/**
	 * @see #sireId
	 */
	public long getSireId() {
		return sireId;
	}

	/**
	 * @see #sireId
	 */
	public void setSireId(long sireId) {
		this.sireId = sireId;
	}

	/**
	 * @see #damId
	 */
	public long getDamId() {
		return damId;
	}

	/**
	 * @see #damId
	 */
	public void setDamId(long damId) {
		this.damId = damId;
	}

	/**
	 * @see #offsprings
	 */
	public Set<Long> getOffsprings() {
		return new HashSet<>(offsprings);
	}

	/**
	 * @see #offsprings
	 */
	public void addOffspring(Long... offsprings) {
		this.offsprings.addAll(Arrays.asList(offsprings));
	}

	/**
	 * @see #offsprings
	 */
	public void addOffsprings(Collection<Long> offsprings) {
		this.offsprings.addAll(offsprings);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id).add("name", name).add("ems", ems).add("gender", gender).add("dob", dob)
				.add("nationalityCountryCode", nationalityCountryCode).add("locationCountryCode", locationCountryCode)
				.add("title", title).add("inbreeding", inbreeding).add("sire", sireId).add("dam", damId)
				.toString();
	}


	/**
	 * Gender definition, {@link Gender#M} for male and {@link Gender#M} for female.
	 *
	 * @author padriano
	 *
	 */
	public enum Gender {
		M {
			@Override
			public String asParent() {
				return "Sire";
			}
		},
		F {
			@Override
			public String asParent() {
				return "Dam";
			}
		};

		public abstract String asParent();
	}
}
