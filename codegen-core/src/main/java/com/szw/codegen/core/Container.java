package com.szw.codegen.core;

import com.szw.codegen.core.util.NullCollection;

import java.util.Collection;
import java.util.Iterator;

/**
 * 容器类，是存放数据T的容器，具有主动加载和缓存的特点。
 *
 * @author SZW
 */
public interface Container<T> extends Iterable<T> {

	/**
	 * 获取数据集合。
	 * <p>
	 * 方法的实现必须遵守以下约定：
	 * <ul>
	 *     <li>返回结果为null，表示容器为空，尚未加载数据；</li>
	 *     <li>结果不为null，表示容器不为空，数据已加载。</li>
	 * </ul>
	 *
	 * @return 容器中保存的数据集合
	 */
	Collection<T> getCollection();

	/**
	 * 加载数据到容器
	 */
	void load();

	@Override
	default Iterator<T> iterator() {
		Collection<T> collection = getCollection ();

		if (collection == null) {
			load ();

			collection = getCollection ();

			// load 后仍为空
			if (collection == null) {
				collection = NullCollection.newInstance ();
			}
		}

		return collection.iterator ();
	}
}
