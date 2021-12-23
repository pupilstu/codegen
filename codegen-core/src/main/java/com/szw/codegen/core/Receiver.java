package com.szw.codegen.core;

import com.szw.codegen.core.entity.Code;

/**
 * 接收器，接收生成的代码，进行下一步处理，例如写入本地文件、写入zip等。
 *
 * @author SZW
 */
public interface Receiver {

	/**
	 * 接收代码文件，进行下一步处理
	 */
	void receive(Code code);
}
