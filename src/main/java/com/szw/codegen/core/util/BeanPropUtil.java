package com.szw.codegen.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author SZW
 * @date 2021/9/4
 */
@Slf4j
public class BeanPropUtil {

	public static class Property<T> {
		private final Method getter;
		private final Method setter;
		private String name;

		public Property(Method getter, Method setter, String name) {
			this.getter = getter;
			this.setter = setter;
			this.name = name;
		}

		public Object get(T t) throws InvocationTargetException, IllegalAccessException {
			return getter.invoke (t);
		}

		public void set(T t, Object val) throws InvocationTargetException, IllegalAccessException {
			setter.invoke (t, val);
		}

		public String getName() {
			return name;
		}

		public Property<T> setName(String name) {
			this.name = name;
			return this;
		}
	}

	public static <T> List<Property<T>> getProperties(Class<T> beanClass, Predicate<Property<T>> filter) {
		return Arrays.stream (BeanUtils.getPropertyDescriptors (beanClass))
				.map (p -> new Property<T> (p.getReadMethod (), p.getWriteMethod (), p.getName ()))
				.filter (filter)
				.collect (Collectors.toList ());
	}

	public static <T> List<Property<T>> getProperties(Class<T> beanClass, boolean setterNotNull) {
		return getProperties (beanClass, p -> !setterNotNull || p.setter != null);
	}

	public static <T> List<Property<T>> getProperties(Class<T> beanClass) {
		return getProperties (beanClass, false);
	}

	public static <T> Property<T> getProperty(String name, Class<T> beanClass) {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor (beanClass, name);
		return pd == null ? null : new Property<> (pd.getReadMethod (), pd.getWriteMethod (), pd.getName ());
	}

	public static <T> boolean exists(String name, T bean) {
		return BeanUtils.getPropertyDescriptor (bean.getClass (), name) != null;
	}

	public static Object get(String name, Object bean) throws InvocationTargetException, IllegalAccessException {
		return Objects.requireNonNull (BeanUtils.getPropertyDescriptor (bean.getClass (), name)).getReadMethod ().invoke (bean);
	}

	public static <T> T set(String name, T bean, Object val) throws InvocationTargetException, IllegalAccessException {
		Objects.requireNonNull (BeanUtils.getPropertyDescriptor (bean.getClass (), name)).getWriteMethod ().invoke (bean, val);
		return bean;
	}
}
