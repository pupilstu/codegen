package com.szw.codegen.core;

import com.szw.codegen.core.entity.Code;
import com.szw.codegen.core.entity.Template;

import java.util.Iterator;

/**
 * @author SZW
 * @date 2021/12/22
 */
class ChainImpl implements Interceptor.Chain {
	private final Object data;
	private final Template template;
	private final Generator.Builder builder;

	private final Iterator<Interceptor> interceptorItr;

	public ChainImpl(Object data, Template template, Generator.Builder builder, Iterator<Interceptor> interceptorItr) {
		this.data = data;
		this.template = template;
		this.builder = builder;
		this.interceptorItr = interceptorItr;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public Template getTemplate() {
		return template;
	}

	@Override
	public Generator.Builder getBuilder() {
		return builder;
	}

	@Override
	public Code proceed(Object data, Template template) {
		if (!interceptorItr.hasNext ())
			throw new AssertionError ("The last interceptor cannot call the method 'process' !");

		Interceptor next = interceptorItr.next ();

		return next.intercept (new ChainImpl (data, template, this.builder, this.interceptorItr));
	}
}
