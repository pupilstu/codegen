package com.szw.codegen.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author SZW
 * @date 2021/9/3
 */
@Data
@Accessors(chain = true)
public class ResultFile {

	/**
	 * 代码存放的目标文件夹
	 */
	private String targetDir;

	/**
	 * 文件名
	 */
	private String filename;

	/**
	 * 内容
	 */
	private String content;
}
