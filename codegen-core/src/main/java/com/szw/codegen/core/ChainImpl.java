package com.szw.codegen.core;

import java.util.Iterator;

/**
 * @author SZW
 * @date 2021/12/22
 */
class ChainImpl<T, R> implements Interceptor.Chain<T, R> {
	private final Iterator<Interceptor<T, R>> interceptorItr;

	ChainImpl(Iterator<Interceptor<T, R>> interceptorItr) {
		this.interceptorItr = interceptorItr;
	}

	@Override
	public R proceed(T template, Object data) {
		if (!interceptorItr.hasNext ()) {
			throw new AssertionError ("The last interceptor cannot call the method 'process' !");
		}

		Interceptor<T, R> next = interceptorItr.next ();

		return next.intercept (template, data, this);
	}
}
