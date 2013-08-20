package com.tri.ui.model.observer;

import com.tri.ui.model.DataController;

/**
 * A semantic event which indicates that a controller defined change event
 * occurred.
 * 
 * <p>
 * {@code ChangeEvent} objects are (almost) immutable. Passed mutable source
 * objects changed via kept references break immutableness of
 * {@code ChangeEvent} objects.
 * </p>
 * 
 * @author khennig@pobox.com
 */
public class ChangeEvent {

	private final DataController<?> source;

	// TODO: Evaluate: Support multiple types
	private final ChangeEventType type;

	/**
	 * Constructor
	 * 
	 * @param source
	 *            event source object
	 * @param type
	 *            event type
	 */
	public ChangeEvent(final DataController<?> source,
			final ChangeEventType type) {
		this.source = source;
		this.type = type;
	}

	public DataController<?> getSource() {
		return source;
	}

	public ChangeEventType getType() {
		return type;
	}

}
