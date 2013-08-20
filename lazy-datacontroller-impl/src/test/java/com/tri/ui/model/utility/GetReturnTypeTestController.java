package com.tri.ui.model.utility;

import java.util.List;

import com.tri.ui.model.PagedListDataController;
import com.tri.ui.model.SortProperty;

public class GetReturnTypeTestController extends
		PagedListDataController<Long, String> {

	private static final long serialVersionUID = 1L;

	@Override
	public int count() {
		return 0;
	}

	@Override
	public List<String> load(int first, int pageSize, List<SortProperty> sorting) {
		return null;
	}

	@Override
	public Long getKeyOf(String value) {
		return null;
	}

}
