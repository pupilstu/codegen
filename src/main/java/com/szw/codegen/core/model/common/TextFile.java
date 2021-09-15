package com.szw.codegen.core.model.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author SZW
 * @date 2021/9/3
 */
@Data
@Accessors(chain = true)
public class TextFile {

	private String parentDir;

	private String filename;

	private String content;
}
