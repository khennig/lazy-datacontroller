package com.tri.ui.model.utility;

import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;

public class BeanProperty {

	/**
	 * Sets a property on a given bean. Due to implementation limitations you
	 * can not set a property to {@code null}. To {@code null} a property use
	 * {@link #clearBeanProperty(Object, String)}.
	 * 
	 * @param bean
	 * @param name
	 * @param value
	 * 
	 * @throws NullPointerException
	 *             if bean and/or name are null
	 */
	public static void setBeanProperty(final Object bean, final String name,
			final Object value) {
		Validate.notNull(bean, "Bean required");
		Validate.notNull(name, "Property name required");

		try {
			BeanUtils.setProperty(bean, name, value);
		} catch (Exception exc) {
			throw new RuntimeException("Failed to set filter property " + name,
					exc);
		}
	}

	/**
	 * Clears a property on a given bean, i.e. sets it {@code null}.
	 * 
	 * @param bean
	 * @param name
	 * 
	 * @throws NullPointerException
	 *             if bean and/or name are null
	 */
	public static void clearBeanProperty(final Object bean, final String name) {
		Validate.notNull(bean, "Bean required");
		Validate.notEmpty(name, "Not empty property name required");

		final String methodName = new StringBuilder("set")
				.append(name.substring(0, 1).toUpperCase())
				.append(name.substring(1)).toString();
		for (Method method : bean.getClass().getMethods()) {
			if (method.getName().equals(methodName)) {
				try {
					method.invoke(bean, (Object) null);
					return;
				} catch (Exception exc) {
					throw new RuntimeException("Failed to clear property: "
							+ name);
				}
			}
		}
		throw new RuntimeException("Setter of property not found: " + name);
	}

	/**
	 * Returns the return type of a given method (ignores bridge methods).
	 * 
	 * @param bean
	 * @param methodName
	 * @return return type
	 * 
	 * @throws NullPointerException
	 *             if bean and/or methodName are null
	 */
	public static Class<?> getReturnType(final Object bean,
			final String methodName) {
		Validate.notNull(bean, "Bean required");
		Validate.notEmpty(methodName, "Not empty method name required");

		for (Method method : bean.getClass().getMethods()) {
			if (method.getName().equals(methodName) && !method.isBridge()) {
				return method.getReturnType();
			}
		}
		throw new IllegalArgumentException("Method " + methodName
				+ " not found in class " + bean.getClass().getName());
	}
}
