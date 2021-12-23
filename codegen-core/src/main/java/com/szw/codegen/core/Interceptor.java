package com.szw.codegen.core;

import com.szw.codegen.core.entity.Code;
import com.szw.codegen.core.entity.Template;

/**
 * 拦截器
 *
 * @author SZW
 * @date 2021/12/19
 */
public interface Interceptor {
	/**
	 * 拦截方法
	 *
	 * @param chain chain
	 * @return 生成的代码文件
	 */
	Code intercept(Chain chain);


	interface Chain {
		/**
		 * 获取数据。对于静态模板，返回值为null
		 *
		 * @return data
		 */
		Object getData();

		/**
		 * 获取模板
		 *
		 * @return 模板
		 */
		Template getTemplate();

		/**
		 * 获取生成器的builder
		 *
		 * @return builder
		 */
		Generator.Builder getBuilder();

		/**
		 * 执行下一个interceptor
		 *
		 * @param data     数据
		 * @param template 模板
		 * @return 生成的代码文件
		 */
		Code proceed(Object data, Template template);
	}
}
