package com.catpeds.model.comparator;

import java.util.Comparator;
import java.util.Set;

import com.catpeds.model.Pedigree;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * {@link Pedigree} {@link Comparator} that uses all its POJO
 * properties.
 *
 * @author padriano
 *
 */
public class PedigreeComparator implements Comparator<Pedigree> {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Pedigree o1, Pedigree o2) {
		int result = ComparisonChain.start()
		.compare(o1.getId(), o2.getId())
		.compare(o1.getTitle(), o2.getTitle(), Ordering.natural().nullsFirst())
		.compare(o1.getName(), o2.getName(), Ordering.natural().nullsFirst())
		.compare(o1.getGender(), o2.getGender(), Ordering.natural().nullsFirst())
		.compare(o1.getEms(), o2.getEms(), Ordering.natural().nullsFirst())
		.compare(o1.getDob(), o2.getDob(), Ordering.natural().nullsFirst())
		.compare(o1.getInbreeding(), o2.getInbreeding())
		.compare(o1.getDamId(), o2.getDamId(), Ordering.natural().nullsFirst())
		.compare(o1.getSireId(), o2.getSireId(), Ordering.natural().nullsFirst())
		.compare(o1.getLocationCountryCode(), o2.getLocationCountryCode(), Ordering.natural().nullsFirst())
		.compare(o1.getNationalityCountryCode(), o2.getNationalityCountryCode(), Ordering.natural().nullsFirst())
		.compare(o1.getOffsprings().size(), o2.getOffsprings().size())
		.result();
		if (result != 0) {
			return result;
		}
		Set<Long> offsprings = o1.getOffsprings();
		offsprings.removeAll(o2.getOffsprings());
		return offsprings.size() == 0 ? 0 : -1; // simplifying - not comparing id's in the sets
	}
}
