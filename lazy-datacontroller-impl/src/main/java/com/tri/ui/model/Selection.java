package com.tri.ui.model;

import java.util.List;

/**
 * Support for selection.
 * 
 * @param <V>
 *            value
 * 
 * @author khennig@pobox.com
 */
public interface Selection<K, V> {

	/**
	 * Returns the key object of given value.
	 * 
	 * @param value
	 * @return value key
	 */
	 K getKeyOf(V value);

	/**
	 * <p>
	 * Returns the value for given key. Default implementation looks for value
	 * in cached data.
	 * </p>
	 * <p>
	 * {@link #getKeyOf(Object)} must be implemented to use the default
	 * implementation of this method.
	 * </p>
	 * 
	 * @param key
	 *            value key
	 * @return found value or null if none was found
	 */
	 V getValueOf(K key);

	/**
	 * Returns selection values as list.
	 * 
	 * @return list of selected values
	 */
	 List<V> getSelection();

	/**
	 * Returns the element at the specified position in the selection list.
	 * 
	 * @param index
	 *            index of the element to return
	 * @return the element at the specified position in this selection list
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index &lt; 0 || index &gt;=
	 *             {@link #getSelectionSize()})
	 */
	 V getSelectionValue(int index);

	/**
	 * Returns the number of elements in the selection
	 * 
	 * @return number of elements in the selection
	 */
	 int getSelectionSize();

	/**
	 * Adds given value to selection.
	 * 
	 * @param values
	 * @throws NullPointerException
	 *             if parameter values is null or has a null element
	 */
	 void addSelection(final List<V> values);

	/**
	 * Adds given value to selection
	 * 
	 * @param value
	 * @throws NullPointerException
	 *             if value is null
	 */
	 void addSelection(final V value);

	/**
	 * Replaces selection with given values.
	 * 
	 * @param values
	 */
	 void setSelection(final List<V> values);

	/**
	 * Replaces selection to given value.
	 * 
	 * @param value
	 */
	 void setSelection(final V value);

	/**
	 * Replaces selection with given one. {@link #setSelection(Object)} updates
	 * internal Selection Index automatically, it has to be specified using this
	 * method.
	 * 
	 * @param value
	 *            new selection
	 * @param selectionIndex
	 *            selectionIndex of parameter value, ignored if parameter value
	 *            is already selected and selection contains only one element.
	 */
	 void setSelection(final V value, int selectionIndex);

	/**
	 * Clears selection observers should be notified. Does nothing if nothing is
	 * selected.
	 */
	 void clearSelection();

	/**
	 * Selects absolute first element (action method).
	 * 
	 * @throws IllegalStateException
	 *             if no element available
	 */
	 void first();

	/**
	 * Selects absolute last element (action method).
	 * 
	 * @throws IllegalStateException
	 *             if no element available
	 */
	 void last();

	/**
	 * Selects next element from current (single) selection (action method).
	 * 
	 * @throws IllegalStateException
	 *             if there is no next element
	 */
	 void next();

	/**
	 * Selects previous element from current (single) selection (action method).
	 * 
	 * @throws IllegalStateException
	 *             if there is no previous element
	 */
	 void previous();

	/**
	 * Returns true if there is an element next to the current selection
	 * selectable via {@link #next()}. Returns false if there is no such
	 * element, nothing is selected at all or the current selection index can
	 * not be determined.
	 */
	 boolean hasNext();

	/**
	 * Returns true if there is a previous element to the current selection
	 * selectable via {@link #previous()}. Returns false if there is no such
	 * element, nothing is selected at all or the current selection index can
	 * not be determined.
	 */
	 boolean hasPrevious();
}
