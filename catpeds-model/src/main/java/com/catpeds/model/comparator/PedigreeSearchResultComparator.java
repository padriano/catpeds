package com.catpeds.model.comparator;

import java.util.Comparator;

import com.catpeds.model.PedigreeSearchResult;
import com.google.common.collect.ComparisonChain;

/**
 * {@link PedigreeSearchResult} {@link Comparator} that uses all its POJO
 * properties.
 *
 * @author padriano
 *
 */
public class PedigreeSearchResultComparator implements Comparator<PedigreeSearchResult> {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(PedigreeSearchResult o1, PedigreeSearchResult o2) {
		return ComparisonChain.start()
		.compare(o1.getId(), o2.getId())
		.compare(o1.getTitle(), o2.getTitle())
		.compare(o1.getName(), o2.getName())
		.compare(o1.getGender(), o2.getGender())
		.compare(o1.getEms(), o2.getEms())
		.compare(o1.getDob(), o2.getDob())
		.result();
	}
}
