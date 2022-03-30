package com.szw.codegen.core;

/**
 * 模板与数据的合并策略。
 *
 * @param <T> 模板类型
 * @param <R> 结果类型
 * @author SZW
 */
public interface MergeStrategy<T, R> {

	/**
	 * 执行合并策略。如果这个方法的返回值不为null，则不会执行{@link #doMerge(Object, Object, Object, Engine)}方法。
	 * <p>
	 * 引入这个方法，主要是为了支持静态模板，即只需与静态数据结合的模板。
	 *
	 * @param template   模板
	 * @param staticData 静态数据
	 * @param engine     使用的模板引擎
	 * @return 为null，继续执行{@link #doMerge(Object, Object, Object, Engine)}；不为null，说明该模板与 staticData 合并即可得到结果，则不会执行{@link #doMerge(Object, Object, Object, Engine)}方法
	 */
	default R doMerge(T template, Object staticData, Engine engine) {
		return null;
	}

	/**
	 * 执行合并策略
	 *
	 * @param template   模板
	 * @param data       数据
	 * @param staticData 静态数据
	 * @param engine     使用的模板引擎
	 * @return 结果
	 */
	R doMerge(T template, Object data, Object staticData, Engine engine);

}
