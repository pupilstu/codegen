package com.szw.codegen.core.model;

import lombok.Data;

/**
 * @author SZW
 */
@Data
public class TemplateFile {
	/**
	 * 指示模板是否是静态模板，静态模板只与静态数据（staticData）结合一次（不与 data 结合），生成一份代码文件。
	 */
	private boolean staticTemplate;

	/**
	 * 模板文件路径
	 */
	private String filePath;

	/**
	 * 生成结果存放的目标文件夹
	 */
	private String targetDir;

	/**
	 * 生成结果的文件名，可使用模板语法
	 */
	private String targetFilename;

	/**
	 * 模板内容，配置了模板文件路径，会自动从文件中读取
	 */
	private String content;
}
