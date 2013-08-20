package com.tri.ui.test.interfaces.sample.web;

import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import com.tri.ui.model.ListDataController;
import com.tri.ui.model.SortProperty;
import com.tri.ui.model.adapter.PrimeFacesListAdapter;
import com.tri.ui.model.observer.ChangeEventType;
import com.tri.ui.test.domain.model.sample.Sample;
import com.tri.ui.test.domain.model.sample.SampleRepository;

public class ListSampleDataController extends ListDataController<Long, Sample> {

	private static final long serialVersionUID = 1L;

	@Inject
	SampleRepository sampleRepository;

	/** filter */
	String name;

	/** filter */
	Long age;

	PrimeFacesListAdapter<Long, Sample> adapter;

	@Override
	public List<Sample> load(List<SortProperty> sorting) {
		return sampleRepository.findSamples(0, Integer.MAX_VALUE, name, age,
				null);
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

	public PrimeFacesListAdapter<Long, Sample> getAdapter() {
		if (adapter == null) {
			adapter = new PrimeFacesListAdapter<Long, Sample>(this);
		}
		return adapter;
	}

	@Override
	public Long getKeyOf(Sample value) {
		return value.getId();
	}

}
