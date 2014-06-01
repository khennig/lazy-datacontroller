package com.tri.ui.model.observer;

public interface Observable {

	/**
	 * Adds an observer to the list of observers for this object, provided that
	 * it is not the same as some observer already in the list.
	 * 
	 * @param observer
	 *            an observer to be added
	 * @throws NullPointerException
	 *             if parameter observer is null
	 * @throws IllegalArgumentException
	 *             if parameter observer is already in the list
	 */
	public void addObserver(Observer observer);

	/**
	 * Removes an observer from the list of observers of this object.
	 * 
	 * @param observer
	 *            observer to be removed
	 * @throws NullPointerException
	 *             if parameter observer is null
	 * @throws IllegalArgumentException
	 *             if parameter observer is not in the list
	 */
	public void removeObserver(Observer observer);

	/**
	 * Clears the observer list so that this object no longer has any observers.
	 */
	public void removeObservers();

	/**
	 * Notifies all observers. The order in which notifications will be
	 * delivered is unspecified.
	 * 
	 * @param event
	 *            event to deliver
	 */
	public void notify(ChangeEvent event);

	/**
	 * Notifies all observers. The order in which notifications will be
	 * delivered is unspecified. Constructs a new ChangeEvent of given type and
	 * this object as source.
	 * 
	 * @param type
	 *            type of event
	 */
	public void notify(ChangeEventType type);
}
