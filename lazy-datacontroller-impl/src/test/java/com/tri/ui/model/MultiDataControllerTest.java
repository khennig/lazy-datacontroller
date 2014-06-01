package com.tri.ui.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MultiDataControllerTest {

	@Test(expected = IllegalStateException.class)
	public void getIndexOfNoValues() {
		// setup
		TestMultiDataController dc = new TestMultiDataController(
				generateTestData(0));
		// test, check
		dc.getIndexOf(new TestValue(1L));
	}

	@Test
	public void getIndexOfCertainValues() {
		// setup
		TestMultiDataController dc = new TestMultiDataController(
				generateTestData(60));
		for (int testData : new int[] { 0, 1, 59 }) {
			// test, assert
			assertThat(dc.getIndexOf(dc.getData().get(testData))).isEqualTo(
					testData);
		}
	}

	@Test
	public void hasNext() {

		// setup: 0
		TestMultiDataController dc = new TestMultiDataController(
				generateTestData(0));
		// test, check
		assertThat(dc.hasNext()).isFalse();

		// setup: {first, expected page}
		dc = new TestMultiDataController(generateTestData(5));
		for (Object[] testData : new Object[][] { { 0, true }, { 2, true },
				{ 4, false } }) {
			dc.setSelection(dc.getData().get((int) testData[0]));
			assertThat(dc.hasNext()).isEqualTo((boolean) testData[1]);
		}

	}

	@Test
	public void hasPrevious() {

		// setup: 0
		TestMultiDataController dc = new TestMultiDataController(
				generateTestData(0));
		// test, check
		assertThat(dc.hasPrevious()).isFalse();

		// setup: {first, expected page}
		dc = new TestMultiDataController(generateTestData(5));
		for (Object[] testData : new Object[][] { { 4, true }, { 2, true },
				{ 0, false } }) {
			dc.setSelection(dc.getData().get((int) testData[0]));
			assertThat(dc.hasPrevious()).isEqualTo((boolean) testData[1]);
		}

	}

	/**
	 * Returns a List of {@link TestValue}s.
	 * 
	 * @param count
	 *            number {@link TestValue}s to generate
	 * @return list
	 */
	public List<TestValue> generateTestData(int count) {
		List<TestValue> result = new ArrayList<TestValue>(count);
		for (long i = 1; i <= count; i++) {
			result.add(new TestValue(i));
		}
		return result;
	}

	@SuppressWarnings("serial")
	public static class TestMultiDataController extends
			ListDataController<Long, TestValue> {

		List<TestValue> testValues;

		Long testSize;

		public TestMultiDataController(List<TestValue> testValues) {
			this.testSize = (long) testValues.size();
			this.testValues = testValues;
		}

		@Override
		public List<TestValue> load(List<SortProperty> sorting) {
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
