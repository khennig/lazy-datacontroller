package com.tri.ui.test.interfaces.sample.web;

import com.tri.ui.model.ListDataController;
import com.tri.ui.model.SingleDataController;
import com.tri.ui.model.observer.ChangeEvent;
import com.tri.ui.model.observer.ChangeEventType;
import com.tri.ui.model.observer.Observer;
import com.tri.ui.test.domain.model.sample.Sample;

public class DetailSampleDataController extends SingleDataController<Sample>
		implements Observer {

	private static final long serialVersionUID = 1L;

	ListDataController<Long, Sample> samples;

	@Override
	public Sample load() {
		return (samples.getSelectionSize() == 1) ? samples.getSelection()
				.get(0) : null;
	}

	@Override
	public void update(final ChangeEvent event) {
		if (event.getSource() == samples
				&& event.getType() == ChangeEventType.SELECTION) {
			clearCache();
		}
	}

	public void setSamples(final ListDataController<Long, Sample> samples) {
		this.samples = samples;
	}

}
