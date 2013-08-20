package com.tri.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.tri.ui.model.observer.ChangeEvent;
import com.tri.ui.model.observer.ChangeEventType;
import com.tri.ui.model.observer.Observable;
import com.tri.ui.model.observer.Observer;

/**
 * Super class for all data controllers.
 * 
 * @param <D>
 *            data
 * 
 * @author khennig@pobox.com
 */
public abstract class DataController<D> implements Observable, Serializable {

	private static final long serialVersionUID = 1L;

	private List<Observer> observers;

	/** Returns the value element(s) of this data controller */
	public abstract D getData();

	@Override
	public void addObserver(final Observer observer) {
		Validate.notNull(observer, "Observer required");
		Validate.isTrue(!getObservers().contains(observer),
				"Observer already associated");
		if (!getObservers().add(observer)) {
			throw new RuntimeException("Failed to add observer");
		}
	}

	@Override
	public void removeObserver(final Observer observer) {
		Validate.notNull(observer, "Observer required");
		Validate.isTrue(getObservers().remove(observer),
				"Observer not associated");
	}

	@Override
	public void removeObservers() {
		getObservers().clear();
	}

	@Override
	public void notify(final ChangeEvent event) {
		Validate.notNull(event, "Event required");
		for (Observer observer : getObservers()) {
			observer.update(event);
		}
	}

	@Override
	public void notify(final ChangeEventType type) {
		notify(new ChangeEvent(this, type));
	}

	private List<Observer> getObservers() {
		if (observers == null) {
			observers = new ArrayList<Observer>();
		}
		return observers;
	}

}
