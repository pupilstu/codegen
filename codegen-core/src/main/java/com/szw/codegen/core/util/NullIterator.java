package com.szw.codegen.core.util;

import java.util.Iterator;

/**
 * 遍历器空对象实现
 *
 * @author SZW
 * @date 2021/12/22
 */
public class NullIterator<T> implements Iterator<T> {

	public static <E> NullIterator<E> newInstance() {
		return new NullIterator<> ();
	}

	private NullIterator() {
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public T next() {
		return null;
	}
}
