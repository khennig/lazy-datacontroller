package com.tri.ui.test.interfaces.sample.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import com.tri.ui.model.PagedListDataController;
import com.tri.ui.model.SortProperty;
import com.tri.ui.model.adapter.PrimeFacesLazyAdapter;
import com.tri.ui.model.observer.ChangeEventType;
import com.tri.ui.test.domain.model.sample.Sample;
import com.tri.ui.test.domain.model.sample.SampleRepository;

public class PagedSampleDataController extends
		PagedListDataController<Long, Sample> {

	private static final long serialVersionUID = 1L;

	@Inject
	SampleRepository sampleRepository;

	PrimeFacesLazyAdapter<Long, Sample> adapter;

	/** filter */
	String name;

	/** filter */
	Long age;

	@Override
	public int count() {
		return (int) sampleRepository.countSamples(name, age);
	}

	@Override
	public List<Sample> load(int first, int pageSize, List<SortProperty> sorting) {
		ArrayList<String> convertedSorting = null;
		if (sorting.size() > 0) {
			convertedSorting = new ArrayList<String>(getSorting().size());
			for (SortProperty sortingProperty : sorting) {
				convertedSorting.add(sortingProperty.getName() + " "
						+ sortingProperty.getOrder().getSqlKeyword());
			}
		}
		return sampleRepository.findSamples(first, pageSize, name, age,
				convertedSorting);
	}

	public void clearAllFilterProperties() {
		name = null;
		age = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public void filterValueChangeListener(ValueChangeEvent event) {
		clearCache();
		clearSelection();
		notify(ChangeEventType.DATA);
	}

	public PrimeFacesLazyAdapter<Long, Sample> getAdapter() {
		if (adapter == null) {
			adapter = new PrimeFacesLazyAdapter<Long, Sample>(this)
					.setNotifyOnSortOrderChanges(true)
					.setClearSelectionOnFilterChanges(true);
		}
		return adapter;
	}

	@Override
	public Long getKeyOf(Sample value) {
		return value.getId();
	}

}
