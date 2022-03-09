package com.szw.codegen.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Locale;

/**
 * 数据源，一个数据的容器。
 * <p>
 * 生成器会从模板源中依次取出数据与模板相结合。
 *
 * @author SZW
 */
public interface DataSource<T> extends Iterable<T> {

	/**
	 * 数据名，默认为{@code T}类名小写
	 *
	 * @return 数据名
	 */
	default String getDataName() {
		Type[] genericInterfaces = getClass ().getGenericInterfaces ();
		for (Type genericInterface : genericInterfaces) {
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
				if (parameterizedType.getRawType ().equals (DataSource.class)) {
					return ((Class<?>) parameterizedType.getActualTypeArguments ()[0])
							.getSimpleName ()
							.toLowerCase (Locale.ROOT);
				}
			}
		}
		return "data";
	}

	/**
	 * 数据迭代器，需要多次调用此方法，请考虑将内部的数据集合进行缓存
	 *
	 * @return 数据迭代器
	 */
	@Override
	Iterator<T> iterator();
}
