package com.tri.ui.model.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.tri.ui.model.PagedListDataController;
import com.tri.ui.model.SortProperty;

public class PrimeFacesPagedListAdapterTest {

	@Test
	public void handleFilters() {

		// setup: single filter value
		TestPagedDataController dc = new TestPagedDataController(
				generateTestData(0));
		PrimeFacesLazyAdapter<Long, TestValue> dca = new PrimeFacesLazyAdapter<Long, TestValue>(
				dc);
		Map<String, Object> newFilters = new HashMap<>();
		newFilters.put("filter1", "VALUE_1");
		// test, check
		assertThat(dca.handleFilters(newFilters)).isTrue();
		assertThat(dca.lastFilters).hasSize(1);
		assertThat(dca.lastFilters.get("filter1")).isEqualTo("VALUE_1");
		assertThat(dc.getFilter1()).isEqualTo("VALUE_1");

		// setup: second filter value added
		newFilters = new HashMap<>();
		newFilters.put("filter1", "VALUE_1");
		newFilters.put("filter2", "2");
		// test, check
		assertThat(dca.handleFilters(newFilters)).isTrue();
		assertThat(dca.lastFilters).hasSize(2);
		assertThat(dca.lastFilters.get("filter1")).isEqualTo("VALUE_1");
		assertThat(dc.getFilter1()).isEqualTo("VALUE_1");
		assertThat(dca.lastFilters.get("filter2")).isEqualTo("2");
		assertThat(dc.getFilter2()).isEqualTo(2L);

		// setup: filter value changed
		newFilters = new HashMap<>();
		newFilters.put("filter1", "VALUE_1");
		newFilters.put("filter2", "21");
		// test, check
		assertThat(dca.handleFilters(newFilters)).isTrue();
		assertThat(dca.lastFilters).hasSize(2);
		assertThat(dca.lastFilters.get("filter1")).isEqualTo("VALUE_1");
		assertThat(dc.getFilter1()).isEqualTo("VALUE_1");
		assertThat(dca.lastFilters.get("filter2")).isEqualTo("21");
		assertThat(dc.getFilter2()).isEqualTo(21L);

		// setup: filter value removed
		newFilters = new HashMap<>();
		newFilters.put("filter2", "21");
		// test, check
		assertThat(dca.handleFilters(newFilters)).isTrue();
		assertThat(dca.lastFilters).hasSize(1);
		assertThat(dca.lastFilters.get("filter1")).isNull();
		assertThat(dc.getFilter1()).isNull();
		assertThat(dca.lastFilters.get("filter2")).isEqualTo("21");
		assertThat(dc.getFilter2()).isEqualTo(21L);

		// setup: all filters removed
		newFilters = new HashMap<>();
		// test, check
		assertThat(dca.handleFilters(newFilters)).isTrue();
		assertThat(dca.lastFilters).isEmpty();
		assertThat(dca.lastFilters.get("filter1")).isNull();
		assertThat(dc.getFilter1()).isNull();
		assertThat(dca.lastFilters.get("filter2")).isNull();
		assertThat(dc.getFilter2()).isNull();

	}

	public List<TestValue> generateTestData(int count) {
		List<TestValue> result = new ArrayList<TestValue>(count);
		for (long i = 1; i <= count; i++) {
			result.add(new TestValue(i));
		}
		return result;
	}

	@SuppressWarnings("serial")
	public static class TestPagedDataController extends
			PagedListDataController<Long, TestValue> {

		List<TestValue> testValues;

		Integer testSize;

		String filter1;

		Long filter2;

		public TestPagedDataController(List<TestValue> testValues) {
			this.testSize = testValues.size();
			this.testValues = testValues;
		}

		@Override
		public int count() {
			return testSize;
		}

		@Override
		public List<TestValue> load(int first, int pageSize,
				List<SortProperty> sorting) {
			return testValues;
		}

		@Override
		public TestValue getValueOf(Long key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Long getKeyOf(TestValue value) {
			throw new UnsupportedOperationException();
		}

		public String getFilter1() {
			return filter1;
		}

		public void setFilter1(String filter1) {
			this.filter1 = filter1;
		}

		public Long getFilter2() {
			return filter2;
		}

		public void setFilter2(Long filter2) {
			this.filter2 = filter2;
		}

	}

	public static class TestValue {

		private Long id;

		public TestValue(Long id) {
			this.id = id;
		}

		public Long getId() {
			return id;
		}
	}
}
