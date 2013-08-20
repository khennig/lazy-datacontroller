package com.tri.ui.test.domain.model.sample;

import java.util.List;

public interface SampleRepository {

	public List<Sample> findSamples(int first, int max, String name, Long age,
			List<String> sorting);

	public long countSamples(String name, Long age);

	public void storeSample(Sample sample);

	public void removeSample(Sample sample);

}
