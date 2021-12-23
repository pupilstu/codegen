package com.szw.codegen.core.util;

import com.szw.codegen.core.Interceptor;
import com.szw.codegen.core.entity.Code;

/**
 * @author SZW
 * @date 2021/12/22
 */
public class NullInterceptor implements Interceptor {
	@Override
	public Code intercept(Chain chain) {
		return chain.proceed (chain.getData (), chain.getTemplate ());
	}
}
