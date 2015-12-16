package com.tri.ui.model;

import java.io.Serializable;

import com.tri.ui.model.utility.Validate;

public class SortProperty implements Serializable {

	private static final long serialVersionUID = 1L;

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SortProperty other = (SortProperty) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (order != other.order)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder(name).append(" ").append(order).toString();
	}

}
