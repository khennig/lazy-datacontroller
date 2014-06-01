package com.tri.ui.model.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.tri.ui.model.DataController;
import com.tri.ui.model.ListDataController;
import com.tri.ui.model.PagedListDataController;
import com.tri.ui.model.SortProperty;
import com.tri.ui.model.SortPropertyOrder;
import com.tri.ui.model.observer.ChangeEvent;
import com.tri.ui.model.observer.ChangeEventType;
import com.tri.ui.model.utility.BeanProperty;

/**
 * <p>
 * Adapter for a {@link DataController} (adaptee) to interoperate with
 * PrimeFaces {@link LazyDataModel} (target).
 * </p>
 * <p>
 * Remember to
 * </p>
 * <ul>
 * <li>set the {@code p:dataTable} parameters {@code p:dataTable.lazy} to
 * {@code true}</li>
 * <li>{@code p:dataTable.rows} &gt; 0 and</li>
 * <li>implement {@link ListDataController#getKeyOf(Object)} (using
 * {@code p:dataTable.rowKey} is not sufficient for lazy loaded paged data)</li>
 * <li>only Primitive Class filters that have a string representation are
 * supported, the property must have a setter and a getter on the DataController
 * </li>
 * </ul>
 * 
 * @author khennig@pobox.com
 * 
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
public class PrimeFacesLazyAdapter<K, V> extends LazyDataModel<V> {

	private static final long serialVersionUID = 1L;

	private static final String DC_GETROWKEY_METHOD = "getKeyOf";

	final PagedListDataController<K, V> adaptee;

	final HashMap<String, String> lastFilters = new HashMap<String, String>();

	List<SortProperty> lastSorting = null;

	// TODO: Evaluate: SortMeta is not serializable, is that an issue?
	List<SortMeta> multiSortBy;

	private final Class<?> getkeyofParameterType;

	/**
	 * Configuration property, if true, call
	 * {@link PagedListDataController#notify(ChangeEvent)} with
	 * {@link ChangeEventType#DATA} on sort order changes
	 */
	boolean notifyOnSortOrderChanges = false;

	/**
	 * Configuration property, if true, call
	 * {@link PagedListDataController#notify(ChangeEvent)} with
	 * {@link ChangeEventType#SELECTION} on filter changes
	 */
	boolean clearSelectionOnFilterChanges = false;

	/**
	 * Constructor
	 * 
	 * @param adaptee
	 */
	public PrimeFacesLazyAdapter(final PagedListDataController<K, V> adaptee) {
		Validate.notNull(adaptee, "Adaptee required");
		this.adaptee = adaptee;
		getkeyofParameterType = BeanProperty.getReturnType(adaptee,
				DC_GETROWKEY_METHOD);
	}

	/**
	 * If set to true adapter calls
	 * {@link PagedListDataController#notify(ChangeEvent)} with
	 * {@link ChangeEventType#DATA} on sort order changes. Default is
	 * {@code false}.
	 */
	public PrimeFacesLazyAdapter<K, V> setNotifyOnSortOrderChanges(
			boolean notifyOnSortOrderChanges) {
		this.notifyOnSortOrderChanges = notifyOnSortOrderChanges;
		return this;
	}

	/**
	 * If set to true adapter calls
	 * {@link PagedListDataController#notify(ChangeEvent)} with
	 * {@link ChangeEventType#DATA} on sort order changes. Default is
	 * {@code false}
	 */
	public PrimeFacesLazyAdapter<K, V> setClearSelectionOnFilterChanges(
			boolean clearSelectionOnFilterChanges) {
		this.clearSelectionOnFilterChanges = clearSelectionOnFilterChanges;
		return this;
	}

	/**
	 * Returns the associated data controller.
	 */
	public PagedListDataController<K, V> getAdaptee() {
		return adaptee;
	}

	/**
	 * PF load for single sort support
	 */
	@Override
	public List<V> load(final int first, final int pageSize,
			final String sortField, final SortOrder sortOrder,
			final Map<String, String> filters) {
		// convert single sort property to List<SortMeta>
		List<SortMeta> multiSortMeta = null;
		if (sortField != null) {
			multiSortMeta = new ArrayList<SortMeta>(1);
			multiSortMeta.add(new SortMeta(null, sortField, sortOrder, null));
		}

		return load(first, pageSize, multiSortMeta, filters);
	}

	/**
	 * PF load for multiple sort support
	 */
	@Override
	public List<V> load(final int first, final int pageSize,
			final List<SortMeta> multiSortMeta,
			final Map<String, String> filters) {

		final boolean filtersChanged = handleFilters(filters);
		final boolean sortOrderChanged = handleSortOrder(multiSortMeta);
		boolean notify = false;

		// clear cache
		if (filtersChanged || sortOrderChanged) {
			adaptee.clearCache();
			notify = filtersChanged || notifyOnSortOrderChanges;
		}

		adaptee.setPageSize(pageSize);
		final int size = adaptee.getSize();
		adaptee.setFirst(first >= size ? Math.max(0, size - 1) : first);
		setRowCount(size);

		// notify observers
		if (filtersChanged && clearSelectionOnFilterChanges) {
			adaptee.clearSelection();
		}
		if (notify) {
			adaptee.notify(ChangeEventType.DATA);
		}

		return adaptee.getData();
	}

	/**
	 * Clears previously passed filters, sets new filters on adaptee.
	 * 
	 * @param filters
	 * @return true if filters have changed to last call
	 */
	boolean handleFilters(Map<String, String> filters) {
		Validate.notNull(filters, "Filters required");
		boolean changed = false;

		// clear non active filters and remove them from lastFilters
		for (Object filterKey : CollectionUtils.subtract(lastFilters.keySet(),
				filters.keySet())) {
			BeanProperty.clearBeanProperty(adaptee, (String) filterKey);
			lastFilters.remove(filterKey);
			changed = true;
		}

		// set changed active filters
		for (Entry<String, String> entry : filters.entrySet()) {
			if (!lastFilters.containsKey(entry.getKey())
					|| !entry.getValue()
							.equals(lastFilters.get(entry.getKey()))) {
				BeanProperty.setBeanProperty(adaptee, entry.getKey(),
						entry.getValue());
				lastFilters.put(entry.getKey(), entry.getValue());
				changed = true;
			}
		}

		return changed;
	}

	/**
	 * Updates sorting on the adaptee.
	 * 
	 * @param sortOrder
	 * @return true if sorting has changed to last call
	 */
	boolean handleSortOrder(List<SortMeta> sortOrder) {
		boolean changed = false;

		if (sortOrder == null) {
			if (multiSortBy != null) {
				adaptee.clearSorting();
				multiSortBy = null;
				lastSorting = null;
				changed = true;
			}
		} else {
			final List<SortProperty> newSorting = convert2SortProperty(sortOrder);
			if (!newSorting.equals(lastSorting)) {
				adaptee.setSorting(newSorting);
				multiSortBy = sortOrder;
				lastSorting = newSorting;
				changed = true;
			}
		}

		return changed;
	}

	/**
	 * Converts a list of PrimeFaces SortMeta to a list of DC SortPropertyOrder
	 * 
	 * @param sortMetas
	 * @return converted list
	 * @throws IllegalArgumentException
	 *             if no conversion possible
	 */
	List<SortProperty> convert2SortProperty(final List<SortMeta> sortMetas) {
		List<SortProperty> sortProperties = new ArrayList<SortProperty>(
				sortMetas.size());
		for (SortMeta sortMeta : sortMetas) {
			final SortOrder order = sortMeta.getSortOrder();
			if (order == SortOrder.ASCENDING || order == SortOrder.DESCENDING) {
				sortProperties.add(new SortProperty(sortMeta.getSortField(),
						convert2SortPropertyOrder(order)));
			}
		}
		return sortProperties;
	}

	/**
	 * Converts PrimeFaces SortOrder to DC SortPropertyOrder.
	 * 
	 * @param order
	 * @return converted SortOrder
	 * @throws IllegalArgumentException
	 *             if no conversion possible
	 */
	SortPropertyOrder convert2SortPropertyOrder(final SortOrder order) {
		if (order == SortOrder.ASCENDING) {
			return SortPropertyOrder.ASCENDING;
		} else if (order == SortOrder.DESCENDING) {
			return SortPropertyOrder.DESCENDING;
		} else {
			throw new IllegalArgumentException("Unknown SortOrder: " + order);
		}
	}

	@Override
	public V getRowData(final String rowKey) {
		// TODO: Implement: Cache dc return type
		return adaptee.getValueOf(convertToGetValueOfParameter(rowKey));
	}

	@Override
	public K getRowKey(final V object) {
		return adaptee.getKeyOf(object);
	}

	/**
	 * Converts a string based Row Key to the Key Type as specified by the
	 * DataController (adaptee).
	 * 
	 * @param rowKey
	 *            string based Row Key
	 * @return the converted key
	 */
	@SuppressWarnings("unchecked")
	K convertToGetValueOfParameter(final String rowKey) {
		final Object parameterValue = ConvertUtils.convert(rowKey,
				getkeyofParameterType);
		if (parameterValue.getClass().equals(getkeyofParameterType)) {
			return (K) parameterValue;
		} else {
			throw new RuntimeException("Unable to convert String "
					+ "based rowKey to " + getkeyofParameterType.getName());
		}
	}

	public void setMultiSelection(final V[] selection) {
		adaptee.setSelection(Arrays.asList(selection));
	}

	@SuppressWarnings("unchecked")
	public V[] getMultiSelection() {
		return (V[]) adaptee.getSelection().toArray();
	}

	public void setSingleSelection(final V selection) {
		if (selection != null) {
			adaptee.setSelection(selection);
		} else {
			adaptee.clearSelection();
		}
	}

	public V getSingleSelection() {
		final long selectionSize = adaptee.getSelectionSize();
		Validate.validState(selectionSize <= 1, "Multiple selections available");
		return (selectionSize > 0) ? adaptee.getSelectionValue(0) : null;
	}

	public List<SortMeta> getMultiSortBy() {
		return multiSortBy;
	}

	/**
	 * Simply returns first from adaptee
	 */
	public int getFirst() {
		return adaptee.getFirst();
	}

	/**
	 * Simply passes argument to adaptee
	 */
	public void setFirst(int first) {
		adaptee.setFirst(first);
	}

}
