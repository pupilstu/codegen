package com.szw.codegen.basic;

import java.util.Iterator;

/**
 * @author SZW
 * @date 2021/8/21
 */
public interface DataSource extends Iterable<java.lang.Object> {

	Iterator<java.lang.Object> NULL_ITERATOR = new Iterator<java.lang.Object> () {
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public java.lang.Object next() {
			return null;
		}
	};

	/**
	 * 获取数据源配置
	 *
	 * @return beanConfig
	 */
	Object getConfig();

	/**
	 * 返回Data迭代器
	 *
	 * @return Data迭代器
	 */
	@Override
	Iterator<java.lang.Object> iterator();
}
