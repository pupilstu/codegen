package com.szw.codegen.core.util;

import com.szw.codegen.core.Interceptor;

/**
 * 连接器空对象实现
 *
 * @author SZW
 * @date 2021/12/22
 */
@SuppressWarnings("rawtypes")
public class NullInterceptor implements Interceptor {

	@Override
	@SuppressWarnings("unchecked")
	public Object intercept(Object template, Object data, Chain chain) {
		return chain.proceed (template, data);
	}
}
