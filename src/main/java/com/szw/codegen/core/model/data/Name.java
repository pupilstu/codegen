package com.szw.codegen.core.model.data;

import lombok.Data;

import static com.szw.codegen.core.util.StringUtil.*;

/**
 * @author SZW
 * @date 2021/8/28
 */
@Data
public class Name {
	/**
	 * raw name
	 */
	private final String raw;
	/**
	 * UpperCamelCase
	 */
	private final String ucc;
	/**
	 * LowerCamelCase
	 */
	private final String lcc;
	/**
	 * under_score_case
	 */
	private final String usc;

	public Name(String raw) {
		this.raw = raw;
		this.ucc = toUpperCamelCase (raw);
		this.lcc = toLowerCamelCase (raw);
		this.usc = toUnderScoreCase (raw);
	}

	@Override
	public String toString() {
		return raw;
	}
}
