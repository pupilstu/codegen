package com.szw.codegen.basic.engine;

/**
 * @author SZW
 * @date 2021/9/15
 */
public interface Engine {

	void addConfig(String name, Object o);

	<T> String merge(String templateStr, T bean, String beanName);
}
