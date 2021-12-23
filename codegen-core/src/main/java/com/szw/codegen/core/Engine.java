package com.szw.codegen.core;

import java.util.Map;

/**
 * 模板引擎接口
 *
 * @author SZW
 */
public interface Engine {

	/**
	 * 添加静态数据到模板上下文
	 *
	 * @param staticData     staticData
	 * @param staticDataName staticDataName
	 */
	void addStaticData(String staticDataName, Object staticData);

	/**
	 * 添加静态数据到模板上下文
	 *
	 * @param staticData staticData
	 */
	void addStaticData(Map<?, ?> staticData);

	/**
	 * 合并模板与静态数据
	 *
	 * @param template 模板
	 * @return 合并结果
	 */
	String mergeStatic(String template);

	/**
	 * 合并数据与模板，返回结果
	 *
	 * @param template 模板
	 * @param dataName 数据对象在模板中的名字
	 * @param data     数据
	 * @return 合并结果
	 */
	String merge(String template, String dataName, Object data);

	/**
	 * 合并数据与模板，返回结果
	 *
	 * @param template 模板
	 * @param data     数据
	 * @return 合并结果
	 */
	String merge(String template, Map<?, ?> data);
}
