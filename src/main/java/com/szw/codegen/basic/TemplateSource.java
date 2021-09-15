package com.szw.codegen.basic;

import java.util.Iterator;

/**
 * @author SZW
 * @date 2021/8/27
 */
public interface TemplateSource<T> extends Iterable<T> {

	/**
	 * 获取模板源配置
	 *
	 * @return templateConfig
	 */
	default Object getConfig() {
		return null;
	}

	/**
	 * 返回模板迭代器
	 *
	 * @return 模板迭代器
	 */
	@Override
	Iterator<T> iterator();
}
