package com.szw.codegen.basic;

/**
 * 接收器，接收生成的结果，进行下一步处理
 *
 * @author SZW
 * @date 2021/8/29
 */
public interface Receiver<T> {

	/**
	 * 获取接收器配置
	 *
	 * @return templateConfig
	 */
	Object getConfig();

	/**
	 * 接收结果，进行下一步处理
	 *
	 * @param result 结果
	 */
	void receive(T result);
}
