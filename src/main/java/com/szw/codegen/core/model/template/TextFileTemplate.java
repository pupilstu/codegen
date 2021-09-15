package com.szw.codegen.core.model.template;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author SZW
 * @date 2021/9/3
 */
@Data
@Accessors(chain = true)
public class TextFileTemplate {

	/**
	 * 模板文件路径，可以是相对路径（相对于 templateDir），也可以是绝对路径
	 */
	private String templateFilePath;

	/**
	 * 目标文件夹，可以是相对路径（相对于 templateDir），也可以是绝对路径
	 */
	private String targetDir;

	/**
	 * 文件名模板
	 */
	private String filenameTemplate;
}
