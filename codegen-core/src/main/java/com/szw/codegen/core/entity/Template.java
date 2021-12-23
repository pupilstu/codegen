package com.szw.codegen.core.entity;

import lombok.Data;

/**
 * @author SZW
 */
@Data
//@Accessors(chain = true)
//@NoArgsConstructor
public class Template {
	/**
	 * 指示模板是否处于静态域，静态域的模板只与静态数据结合一次，生成一份代码文件
	 */
	private Boolean staticScope;

	/**
	 * 模板文件路径
	 */
	private String templateFilePath;

	/**
	 * 目标文件夹
	 */
	private String targetDir;

	/**
	 * 文件名模板
	 */
	private String filenameTemplate;

	/**
	 * 模板内容
	 */
	private String content;
}
