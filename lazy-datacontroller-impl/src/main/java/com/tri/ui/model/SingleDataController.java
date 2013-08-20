package com.tri.ui.model;

public abstract class SingleDataController<D> extends DataController<D>
		implements Cache {

	private static final long serialVersionUID = 1L;

	private D data;

	/**
	 * Loaded data will be cached internally, call {@link #clearCache()} to
	 * clear cache.
	 * 
	 * @return data object, can be null.
	 */
	public abstract D load();

	@Override
	public D getData() {
		if (data == null) {
			data = load();
		}
		return data;
	}

	@Override
	public void clearCache() {
		data = null;
	}

}
