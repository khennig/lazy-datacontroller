package com.tri.ui.model;

/**
 * Support for pagination.
 * 
 * @author khennig@pobox.com
 */
public interface Pagination {

	/**
	 * Goto first page (action method).
	 * 
	 * @throws IllegalStateException
	 *             if there is no first page
	 */
	public void firstPage();

	/**
	 * Goto last page (action method).
	 * 
	 * @throws IllegalStateException
	 *             if there is no first page
	 */
	public void lastPage();

	/**
	 * Goto next page (action method).
	 * 
	 * @throws IllegalStateException
	 *             if there is no next page
	 */
	public void nextPage();

	/**
	 * Goto previous page (action method).
	 * 
	 * @throws IllegalStateException
	 *             if there is no previous page
	 */
	public void previousPage();

	/**
	 * Returns true if a next page exists.
	 */
	public boolean hasNextPage();

	/**
	 * Returns true if previous page exists.
	 */
	public boolean hasPreviousPage();

	/**
	 * Returns number of pages.
	 * 
	 * @return number of pages, 0 if no page is available
	 */
	public int countPages();

	/**
	 * Returns the current page, zero based
	 */
	public int getPage();

	/**
	 * Moves to given page (action method).
	 * 
	 * @param page
	 *            page number, zero based
	 * @throws IllegalStateException
	 *             if page count is zero
	 * @throws IndexOutOfBoundsException
	 *             if parameter page is not within [0, page count[
	 */
	public void gotoPage(final int page);

	/**
	 * Returns the absolute index of the first element to be displayed in a
	 * page.
	 * 
	 * @return absolute index of the first element in current page, zero based
	 */
	public int getFirst();

	/**
	 * Sets the absolute index of the first element to be displayed in a page.
	 * If given value doesn't match this criteria it will be adjusted.
	 * 
	 * @param first
	 *            absolute index of the first element in a page, zero based
	 * @throws IndexOutOfBoundsException
	 *             if first is not less than absolute data size, 0 always
	 *             allowed
	 */
	public void setFirst(final int first);

	public int getPageSize();

	/**
	 * Sets the number of items to be displayed on a page.
	 * 
	 * @param pageSize
	 *            number of items to be displayed on a page
	 * @throws IllegalArgumentException
	 *             if page size is &lt;= 0
	 */
	public void setPageSize(final int pageSize);
}
