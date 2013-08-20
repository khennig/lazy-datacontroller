package com.tri.ui.model;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tri.ui.model.PagedListDataController;
import com.tri.ui.model.SortProperty;

public class PagedDataControllerTest {

	@Test
	public void getPage() {
		// setup: 0
		TestPagedDataController dc = new TestPagedDataController(
				generateTestData(0));
		// test, check
		assertThat(dc.getPage()).isZero();
		// test, check
		dc.setFirst(0);
		assertThat(dc.getPage()).isZero();

		// setup: {first, expected page}
		dc = new TestPagedDataController(generateTestData(60));
		for (int[] testData : new int[][] { { 0, 0 }, { 1, 0 }, { 14, 0 },
				{ 15, 1 }, { 16, 1 }, { 30, 2 }, { 59, 3 } }) {
			dc.setFirst(testData[0]);
			assertThat(dc.getPage()).isEqualTo(testData[1]);
		}
	}

	@Test
	public void countPages() {
		// setup: {size, expected pages}
		for (int[] testData : new int[][] { { 0, 0 }, { 1, 1 }, { 14, 1 },
				{ 15, 1 }, { 16, 2 }, { 31, 3 } }) {
			TestPagedDataController dc = new TestPagedDataController(
					generateTestData(testData[0]));
			// test, check
			assertThat(dc.countPages()).isEqualTo(testData[1]);
		}
	}

	@Test
	public void gotoPage() {
		// setup: no pages
		TestPagedDataController dc = new TestPagedDataController(
				generateTestData(0));
		// test, check
		try {
			dc.gotoPage(1);
			fail();
		} catch (IllegalStateException exc) {
			// expected
		}

		// setup: {size, page, expected first}
		for (int[] testData : new int[][] { { 1, 0, 0 }, { 30, 0, 0 },
				{ 30, 1, 15 }, { 60, 2, 30 } }) {
			dc = new TestPagedDataController(generateTestData(testData[0]));
			// test, check
			dc.gotoPage(testData[1]);
			assertThat(dc.getFirst()).isEqualTo(testData[2]);
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
	public static class TestPagedDataController extends
			PagedListDataController<Long, TestValue> {

		List<TestValue> testValues;

		Integer testSize;

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
