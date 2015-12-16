package com.tri.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tri.ui.model.observer.ChangeEventType;
import com.tri.ui.model.utility.Validate;

/**
 * <p>
 * List based DataController implementation. Supports:
 * </p>
 * <ul>
 * <li>lazy loading</li>
 * <li>single- and multi-selection</li>
 * <li>sorting</li>
 * </ul>
 * <p>
 * Notifies observers for selection changes. Notice: Sort order changes are not
 * seen as data changes, see {@link #setSorting(List)} etc.
 * </p>
 * 
 * @author khennig@pobox.com
 * 
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
public abstract class ListDataController<K, V> extends DataController<List<V>>
		implements Selection<K, V>, Cache {

	private static final long serialVersionUID = 1L;

	private List<V> selection = new ArrayList<V>();

	private List<V> data;

	int selectionIndex = -1;

	private List<SortProperty> sorting = new ArrayList<SortProperty>();

	/**
	 * Loads data. Loaded data will be cached internally, call
	 * {@link #clearCache()} to clear cache.
	 * 
	 * @param sorting
	 *            sort order
	 * @return List, can be empty but never null.
	 */
	public abstract List<V> load(List<SortProperty> sorting);

	@Override
	public List<V> getData() {
		if (data == null) {
			data = load(sorting);
		}
		return data;
	}

	/**
	 * Returns the number of values in this controller.
	 * 
	 * @return number of values in this controller
	 */
	public int getSize() {
		return getData().size();
	}

	@Override
	public void clearCache() {
		data = null;
	}

	/**
	 * Returns selection values as unmodifiable list.
	 * 
	 * @return list of selected values
	 */
	@Override
	public List<V> getSelection() {
		return Collections.unmodifiableList(selection);
	}

	@Override
	public V getSelectionValue(int index) {
		return selection.get(index);
	}

	@Override
	public int getSelectionSize() {
		return selection.size();
	}

	@Override
	public void addSelection(final List<V> values) {
		Validate.noNullElements(values, "No null values allowed");

		if (!values.isEmpty()) {
			selection.addAll(values);
			updateSelectionIndex();
			notify(ChangeEventType.SELECTION);
		}
	}

	@Override
	public void addSelection(final V value) {
		Validate.notNull(value, "Value required");

		selection.add(value);
		updateSelectionIndex();
		notify(ChangeEventType.SELECTION);
	}

	@Override
	public void setSelection(final List<V> values) {
		Validate.noNullElements(values, "No null values allowed");

		if ((!selection.isEmpty() || !values.isEmpty())
				&& !CollectionUtils.isEqualCollection(selection, values)) {
			selection.clear();
			selection.addAll(values);
			updateSelectionIndex();
			notify(ChangeEventType.SELECTION);
		}
	}

	@Override
	public void setSelection(final V value) {
		Validate.notNull(value, "Value required");

		if (selection.size() != 1 || !selection.contains(value)) {
			selection.clear();
			selection.add(value);
			updateSelectionIndex();
			notify(ChangeEventType.SELECTION);
		}
	}

	@Override
	public void setSelection(final V value, int selectionIndex) {
		Validate.notNull(value, "Value required");

		if (selection.size() != 1 || !selection.contains(value)) {
			Validate.isTrue(selectionIndex >= -1,
					"SelectionIndex %d not >= -1", selectionIndex);
			final int size = getSize();
			Validate.isTrue(selectionIndex < size,
					"SelectionIndex %d not < size: %d", selectionIndex, size);

			selection.clear();
			selection.add(value);
			this.selectionIndex = selectionIndex;
			notify(ChangeEventType.SELECTION);
		}
	}

	@Override
	public void clearSelection() {
		if (!selection.isEmpty()) {
			selection.clear();
			updateSelectionIndex();
			notify(ChangeEventType.SELECTION);
		}
	}

	/**
	 * Updates the internal selection index to reflect current selection.
	 */
	void updateSelectionIndex() {
		if (selection.size() == 1) {
			selectionIndex = getIndexOf(selection.get(0));
		} else {
			selectionIndex = -1;
		}
	}

	/**
	 * Returns the absolute index of the current single selection, or -1 if
	 * there is no selection or there are multiple selections.
	 * 
	 * @return absolute index of current single selection, zero based
	 */
	public int getSelectionIndex() {
		return selectionIndex;
	}

	/**
	 * Returns the index of the first occurrence of the specified value in this
	 * controller, or -1 if this controller does not contain the value.
	 * 
	 * @param value
	 * @return found index, or -1 if value was not found
	 * @throws NullPointerException
	 *             if parameter value is null
	 * @throws IllegalStateException
	 *             if there is no data available
	 */
	int getIndexOf(final V value) {
		Validate.notNull(value, "Value required");
		Validate.validState(getSize() > 0, "No data");

		return getData().indexOf(value);
	}

	/**
	 * <p>
	 * Returns the value for given key. Default implementation looks for value
	 * in caches, see {@link #findChachedValueOf(Object)}.
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
	@Override
	public V getValueOf(K key) {
		return findChachedValueOf(key);
	}

	/**
	 * Find value in cached data.
	 * 
	 * @param key
	 * @return found value or null if non was found
	 */
	public V findChachedValueOf(K key) {

		// check selection
		for (V value : selection) {
			if (getKeyOf(value).equals(key)) {
				return value;
			}
		}

		// check data cache
		for (V value : getData()) {
			if (getKeyOf(value).equals(key)) {
				return value;
			}
		}

		return null;
	}

	@Override
	public void first() {
		Validate.validState(getSize() > 0, "No data");

		setSelection(getData().get(0));
		selectionIndex = 0;
	}

	@Override
	public void last() {
		Validate.validState(getSize() > 0, "No data");

		final int newSelectionIndex = getSize() - 1;
		setSelection(getData().get(newSelectionIndex));
		selectionIndex = newSelectionIndex;
	}

	@Override
	public void next() {
		Validate.validState(hasNext(), "Has no next");

		final int newSelectionIndex = selectionIndex + 1;
		setSelection(getData().get(newSelectionIndex), newSelectionIndex);
	}

	@Override
	public void previous() {
		Validate.validState(hasPrevious(), "Has no previous");

		final int newSelectionIndex = selectionIndex - 1;
		setSelection(getData().get(newSelectionIndex));
		selectionIndex = newSelectionIndex;
	}

	@Override
	public boolean hasNext() {
		if (selectionIndex > -1) {
			return (selectionIndex < getSize() - 1) ? true : false;
		} else {
			return false;
		}
	}

	@Override
	public boolean hasPrevious() {
		if (selectionIndex > -1) {
			return (selectionIndex > 0) ? true : false;
		} else {
			return false;
		}
	}

	public List<SortProperty> getSorting() {
		return sorting;
	}

	/**
	 * Sets sorting, does nothing if sorting will not change. Clears the cache
	 * but does not notify observers for data changes.
	 * 
	 * @param newSorting
	 *            List of SortProperties, replacing
	 */
	public void setSorting(List<SortProperty> newSorting) {
		Validate.notNull(newSorting, "Sorting required");

		if (!sorting.equals(newSorting)) {
			sorting.clear();
			sorting.addAll(newSorting);
			selectionIndex = -1;
			clearCache();
		}
	}

	/**
	 * Clears sorting, does nothing if sorting will not change. Clears the cache
	 * but does not notify observers for data changes.
	 */
	public void clearSorting() {
		if (!sorting.isEmpty()) {
			sorting.clear();
			selectionIndex = -1;
			clearCache();
		}
	}

}
