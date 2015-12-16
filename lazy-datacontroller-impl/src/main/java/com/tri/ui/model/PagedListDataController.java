package com.tri.ui.model;

import java.util.List;

import com.tri.ui.model.utility.Validate;

/**
 * <p>
 * List based DataController implementation. Supports (in addition to
 * {@link ListDataController}:
 * </p>
 * <ul>
 * <li>pagination</li>
 * <li>unlimited data size (up to 2E31 - 1)</li>
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
public abstract class PagedListDataController<K, V> extends
		ListDataController<K, V> implements Pagination {

	private static final long serialVersionUID = 1L;

	/** absolute index of first row in current page, zero based */
	private int first = 0;

	/** page size, default 15 */
	private int pageSize = 15;

	/** absolute data count */
	private Integer size = null;

	/**
	 * Counts absolute data size.
	 * 
	 * @return absolute data size
	 */
	public abstract int count();

	/**
	 * Loads data. Loaded data will be cached internally, call
	 * {@link #clearCache()} to clear cache.
	 * 
	 * @return List, can be empty but never null.
	 */
	public abstract List<V> load(int first, int pageSize,
			List<SortProperty> sorting);

	/**
	 * Loads data. Loaded data will be cached internally, call
	 * {@link #clearCache()} to clear cache.
	 * 
	 * @return List, can be empty but never null.
	 */
	@Override
	public List<V> load(List<SortProperty> sorting) {
		return load(getFirst(), getPageSize(), sorting);
	}

	@Override
	public void clearCache() {
		super.clearCache();
		size = null;
	}

	/**
	 * Returns the absolute data size (number of values).
	 * 
	 * @return absolute data size
	 */
	@Override
	public int getSize() {
		if (size == null) {
			size = count();
		}
		return size;
	}

	@Override
	public void firstPage() throws IllegalStateException {
		Validate.validState(countPages() > 0, "No first page");

		setFirst(0);
	}

	@Override
	public void lastPage() throws IllegalStateException {
		final int pages = countPages();
		Validate.validState(pages > 0, "No last page");

		gotoPage(pages - 1);
	}

	@Override
	public void nextPage() throws IllegalStateException {
		Validate.validState(hasNextPage(), "No next page");

		gotoPage(getPage() + 1);
	}

	/**
	 * Goto previous page.
	 * 
	 * @throws IllegalStateException
	 *             if there is no previous page
	 */
	@Override
	public void previousPage() throws IllegalStateException {
		Validate.validState(hasPreviousPage(), "No previous page");

		gotoPage(getPage() - 1);
	}

	@Override
	public boolean hasNextPage() {
		return getPage() < countPages() - 1;
	}

	@Override
	public boolean hasPreviousPage() {
		return getPage() > 0;
	}

	@Override
	public int countPages() {
		return (int) Math.ceil((double) getSize() / (double) pageSize);
	}

	@Override
	public int getPage() {
		return first / pageSize;
	}

	@Override
	public void gotoPage(final int page) throws IndexOutOfBoundsException,
			IllegalStateException {
		final int pages = countPages();
		Validate.validState(pages > 0, "No pages");
		if (page < 0 || page >= pages) {
			throw new IndexOutOfBoundsException(String.format(
					"Page not in [0, %d[", pages));
		}

		setFirst(pageSize * page);
	}

	@Override
	public int getFirst() {
		return first;
	}

	@Override
	public void setFirst(final int first) throws IndexOutOfBoundsException {
		// adjust to absolute index pointing to first row of page
		final int newFirst = first - (first % pageSize);
		if (this.first != newFirst) {
			final int size = getSize();
			if (first != 0 && (first >= size || first < 0)) {
				throw new IndexOutOfBoundsException(String.format(
						"First not 0 or (> 0 and < %d): %d", size, first));
			}
			this.first = newFirst;
			super.clearCache();
		}
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(final int pageSize) {
		Validate.isTrue(pageSize > 0, "Page Size not > 0: %d", pageSize);

		if (this.pageSize != pageSize) {
			this.pageSize = pageSize;
			setFirst(0);
			super.clearCache();
		}
	}

	/**
	 * <p>
	 * Returns the index of the first occurrence of the specified value in this
	 * controller, or -1 if value was not found. This implementation does a
	 * lookup on the current page's data, i.e. if the value wasn't found it
	 * doesn't mean the value is not contained in the controller.
	 * </p>
	 * <p>
	 * Override this method if you need an absolute lookup.
	 * </p>
	 * 
	 * @param value
	 * @return found index, or -1 if value was not found
	 * @throws NullPointerException
	 *             if parameter value is null
	 * @throws IllegalStateException
	 *             if there is no data available
	 */
	@Override
	int getIndexOf(final V value) {
		Validate.notNull(value, "Value required");
		Validate.validState(getSize() > 0, "No data");

		// search for value in current page
		final List<V> data = getData();
		final int currentPageSize = data.size();
		final K valueKey = getKeyOf(value);
		for (int localIndex = 0; localIndex < currentPageSize; localIndex++) {
			if (getKeyOf(data.get(localIndex)).equals(valueKey)) {
				return first + localIndex;
			}
		}
		return -1;
	}

	@Override
	public void first() throws IllegalStateException {
		Validate.validState(getSize() > 0, "No data");

		gotoPage(0);
		setSelection(getData().get(0));
		selectionIndex = 0;
	}

	@Override
	public void last() throws IllegalStateException {
		Validate.validState(getSize() > 0, "No data");

		lastPage();
		final int newSelectionIndex = getSize() - 1;
		setSelection(getData().get(getData().size() - 1));
		selectionIndex = newSelectionIndex;
	}

	@Override
	public void next() throws IllegalStateException {
		Validate.validState(hasNext(), "Has no next");

		final int newSelectionIndex = selectionIndex + 1;
		setFirst(newSelectionIndex);
		setSelection(getData().get(newSelectionIndex % pageSize));
		selectionIndex = newSelectionIndex;
	}

	@Override
	public void previous() throws IllegalStateException {
		Validate.validState(hasPrevious(), "Has no previous");

		final int newSelectionIndex = selectionIndex - 1;
		setFirst(newSelectionIndex);
		setSelection(getData().get(newSelectionIndex % pageSize));
		selectionIndex = newSelectionIndex;
	}

}
