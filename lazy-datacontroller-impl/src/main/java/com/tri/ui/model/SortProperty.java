package com.tri.ui.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SortProperty {

	private String name;

	private SortPropertyOrder order;

	public SortProperty(final String name, final SortPropertyOrder order) {
		Validate.notNull(name, "Name required");
		Validate.notNull(order, "Order required");
		this.name = name;
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public SortPropertyOrder getOrder() {
		return order;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(order).hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		} else if (other == null || getClass() != other.getClass()) {
			return false;
		}

		final SortProperty cast = (SortProperty) other;
		return new EqualsBuilder().append(name, cast.getName())
				.append(order, cast.getOrder()).isEquals();
	}

	@Override
	public String toString() {
		return new StringBuilder(name).append(" ").append(order).toString();
	}

}
