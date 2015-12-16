package com.tri.ui.model.utility;

import java.util.Iterator;

public final class Validate {

	public static void isTrue(boolean expression, String message,
			Object... args) {
		if (expression == false) {
			throw new IllegalArgumentException(String.format(message, args));
		}
	}

	public static void notNull(final Object object) {
		if (object == null) {
			throw new NullPointerException(String.format("Object is null"));
		}
	}

	public static void notNull(final Object object, final String message,
			final Object... args) {
		if (object == null) {
			throw new NullPointerException(String.format(message, args));
		}
	}

	public static <T extends Iterable<?>> T noNullElements(final T iterable,
			final String message, final Object... args) {
		Validate.notNull(iterable);
		for (final Iterator<?> it = iterable.iterator(); it.hasNext();) {
			if (it.next() == null) {
				throw new IllegalArgumentException(
						String.format(message, args));
			}
		}
		return iterable;
	}

	public static <T extends CharSequence> void notEmpty(final T characters,
			final String message, final Object... args) {
		if (characters == null) {
			throw new NullPointerException(String.format(message, args));
		} else if (characters.length() == 0) {
			throw new IllegalArgumentException(String.format(message, args));
		}
	}

	public static void validState(boolean expression, String message,
			Object... args) {
		if (expression == false) {
			throw new IllegalStateException(String.format(message, args));
		}
	}

	private Validate() {
		// intentionally left blank
	}
}
