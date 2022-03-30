package com.szw.codegen.core;

/**
 * 拦截器
 *
 * @author SZW
 * @date 2021/12/19
 */
public interface Interceptor<T, R> {

	/**
	 * 拦截方法
	 *
	 * @param chain    chain
	 * @param data     data
	 * @param template template
	 * @return 生成的代码文件
	 */
	R intercept(T template, Object data, Chain<T, R> chain);


	interface Chain<T, R> {

		/**
		 * 前进执行下一个interceptor
		 *
		 * @param template 模板
		 * @param data     数据
		 * @return 生成的结果
		 */
		R proceed(T template, Object data);
	}
}
