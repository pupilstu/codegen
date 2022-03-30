package com.szw.codegen.core;

import java.util.Map;

/**
 * 模板引擎的统一抽象，可通过实现该接口来适配不同的模板引擎。
 *
 * @author SZW
 */
public interface Engine {

	/**
	 * 设置非静态数据对象（即 data）在模板环境中的名字
	 *
	 * @param dataName 数据对象在模板中的名字
	 */
	void setDataName(String dataName);

	/**
	 * 添加单个静态数据到模板上下文。
	 * <p>
	 * 在模板中使用 staticData 的格式类似这样：${staticDataName.fieldName}
	 *
	 * @param staticData     staticData
	 * @param staticDataName staticDataName
	 */
	void addStaticData(String staticDataName, Object staticData);

	/**
	 * 添加多个静态数据到模板上下文。
	 * <p>
	 * 以 key 为静态数据在模板中的名字，以 value 为静态数据在模板中的值。
	 * 在模板中使用 data 的格式类似这样：${key}
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
	 * 合并数据与模板，返回结果。
	 * <p>
	 * 使用 dataName (通过{@link #setDataName(String)}方法设置) 为 data 在模板中的名字。
	 * 在模板中使用 data 的格式类似这样：${dataName.fieldName}
	 *
	 * @param template 模板x
	 * @param data     数据
	 * @return 合并结果
	 */
	String merge(String template, Object data);

	/**
	 * 合并数据与模板，返回结果。
	 * <p>
	 * 以 key 为数据在模板中的名字，以 value 为数据在模板中的值。
	 * 在模板中使用 data 的格式类似这样：${key}
	 *
	 * @param template 模板
	 * @param data     数据
	 * @return 合并结果
	 */
	String merge(String template, Map<?, ?> data);
}
