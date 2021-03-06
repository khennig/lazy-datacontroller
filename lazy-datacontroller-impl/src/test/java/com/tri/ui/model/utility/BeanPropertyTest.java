package com.tri.ui.model.utility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BeanPropertyTest {

	@Test
	public void getReturnType() {
		// setup
		GetReturnTypeTestController bean = new GetReturnTypeTestController();
		// test
		Class<?> type = BeanProperty.getReturnType(bean, "getKeyOf");
		// check
		assertThat(type).isEqualTo(Long.class);
	}

}
