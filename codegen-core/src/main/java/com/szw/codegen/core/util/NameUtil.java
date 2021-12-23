package com.szw.codegen.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 命名法工具类，提供大驼峰、小驼峰、下划线和短横线命名法互转
 * <p>
 * 注意：该工具类无法处理不规范的命名和这四种命名法之外的命名，
 * 请务必确保你的命名已经是规范的，且是这四种命名法之一
 *
 * @author SZW
 */
public class NameUtil {

	public static String toUpperCamelCase(String s) {
		return toCamelCase (s, true);
	}

	public static String toLowerCamelCase(String s) {
		return toCamelCase (s, false);
	}

	public static String toUnderScoreCase(String s) {
		return toSeparatorCase (s, '-', '_');

	}

	public static String toKebabCase(String s) {
		return toSeparatorCase (s, '_', '-');
	}

	private static String toCamelCase(String s, boolean isUpper) {
		if (StringUtils.isEmpty (s) || StringUtils.isBlank (s)) {
			return s;
		}
		s = s.trim ();

		StringBuilder builder = new StringBuilder (s.length ());

		char ch = s.charAt (0);
		ch = isUpper ? Character.toUpperCase (ch) : Character.toLowerCase (ch);
		builder.append (ch);

		boolean flag = false;
		for (int i = 1; i < s.length (); i++) {
			ch = s.charAt (i);
			if (ch == '_' || ch == '-') {
				flag = true;
			} else {
				if (flag) {
					ch = Character.toUpperCase (ch);
					flag = false;
				}
				builder.append (ch);
			}
		}

		return builder.toString ();
	}

	private static String toSeparatorCase(String s, char from, char to) {
		if (StringUtils.isEmpty (s) || StringUtils.isBlank (s)) {
			return s;
		}
		s = s.trim ();

		int length = s.length ();
		StringBuilder builder = new StringBuilder (length + (length >> 1));

		char ch = s.charAt (0);
		ch = Character.toLowerCase (ch);
		builder.append (ch);

		boolean flag = false;
		for (int i = 1; i < length; i++) {
			ch = s.charAt (i);
			if (ch == from) {
				ch = to;
			} else if (Character.isUpperCase (ch)) {
				builder.append (to);
				ch = Character.toLowerCase (ch);
			}
			builder.append (ch);
		}

		return builder.toString ();
	}
}
