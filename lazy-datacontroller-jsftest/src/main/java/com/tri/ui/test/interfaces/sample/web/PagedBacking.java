package com.tri.ui.test.interfaces.sample.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.tri.ui.test.domain.model.sample.Sample;
import com.tri.ui.test.domain.model.sample.SampleRepository;

@ManagedBean
@ViewScoped
public class PagedBacking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	SampleRepository sampleRepository;

	@Inject
	PagedSampleDataController sampleIndex;

	@Inject
	DetailSampleDataController sampleDetail;

	Sample sample;

	public Sample getSample() {
		if (sample == null) {
			sample = new Sample();
		}
		return sample;
	}

	public String storeSample() {
		sampleRepository.storeSample(sample);
		sample = null;
		sampleIndex.clearCache();
		return null;
	}

	public PagedSampleDataController getSampleIndex() {
		return sampleIndex;
	}

	public DetailSampleDataController getSampleDetail() {
		return sampleDetail;
	}

	@PostConstruct
	public void init() {
		sampleIndex.addObserver(sampleDetail);
		sampleDetail.setSamples(sampleIndex);
	}

}
