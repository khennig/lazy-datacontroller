package com.tri.ui.model.adapter;

import java.io.Serializable;
import java.util.Arrays;

import javax.faces.model.ListDataModel;

import org.apache.commons.beanutils.ConvertUtils;
import org.primefaces.model.SelectableDataModel;

import com.tri.ui.model.DataController;
import com.tri.ui.model.ListDataController;
import com.tri.ui.model.utility.BeanProperty;
import com.tri.ui.model.utility.Validate;

/**
 * <p>
 * Adapter for a {@link DataController} (adaptee) to interoperate with JSF
 * {@link ListDataModel} and PrimeFaces {@link SelectableDataModel} (targets).
 * </p>
 * <p>
 * Remember to
 * </p>
 * <ul>
 * <li>set the {@code p:dataTable} parameters {@code p:dataTable.lazy} to
 * {@code false}</li>
 * <li>{@code p:dataTable.rows} &gt; 0 and</li>
 * <li>implement {@link ListDataController#getKeyOf(Object)} (using
 * {@code p:dataTable.rowKey} is not sufficient for lazy loaded paged data)</li>
 * </ul>
 * 
 * @author khennig@pobox.com
 * 
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
@SuppressWarnings("serial")
public class PrimeFacesListAdapter<K, V> extends ListDataModel<V> implements
		SelectableDataModel<V>, Serializable {

	private static final String DC_GETROWKEY_METHOD = "getKeyOf";

	private final ListDataController<K, V> adaptee;

	private final Class<?> getkeyofParameterType;

	/**
	 * Constructor
	 * 
	 * @param adaptee
	 */
	public PrimeFacesListAdapter(final ListDataController<K, V> adaptee) {
		super(adaptee.getData());
		this.adaptee = adaptee;
		getkeyofParameterType = BeanProperty.getReturnType(adaptee,
				DC_GETROWKEY_METHOD);
	}

	public ListDataController<K, V> getAdaptee() {
		return adaptee;
	}

	@Override
	public V getRowData(final String rowKey) {
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
		adaptee.setSelection(selection);
	}

	public V getSingleSelection() {
		final long selectionSize = adaptee.getSelectionSize();
		Validate.validState(selectionSize <= 1, "Multiple selections available");
		return (selectionSize > 0) ? adaptee.getSelectionValue(0) : null;
	}

}
