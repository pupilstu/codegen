package com.szw.codegen.core.util;

/**
 * @author SZW
 * @date 2021/8/27
 */
public class StringUtil {

	private static String toCamelCase(String s, boolean isUpper) {
		StringBuilder builder = new StringBuilder (s.length ());

		char c;
		if (s.length () < 1 || (c = s.charAt (0)) == '_') {
			return s;
		}

		if (isUpper && c >= 'a' && c <= 'z') {
			c -= 32;
		} else if (!isUpper && c >= 'A' && c <= 'Z') {
			c += 32;
		}
		builder.append (c);

		boolean flag = false;
		for (int i = 1; i < s.length (); i++) {
			c = s.charAt (i);
			if (c == '_') {
				flag = true;
			} else {
				if (flag) {
					flag = false;
					if (c >= 'a' && c <= 'z') {
						c -= 32;
					}
				}
				builder.append (c);
			}
		}

		return builder.toString ();
	}

	public static String toUpperCamelCase(String s) {
		return toCamelCase (s, true);
	}

	public static String toLowerCamelCase(String s) {
		return toCamelCase (s, false);

	}

	public static String toUnderScoreCase(String s) {
		int length = s.length ();
		StringBuilder builder = new StringBuilder (length + (length >> 1));

		char c;
		if (s.length () < 1 || (c = s.charAt (0)) == '_') {
			return s;
		}

		if (c >= 'A' && c <= 'Z') {
			c += 32;
		}
		builder.append (c);

		boolean flag = false;
		for (int i = 1; i < s.length (); i++) {
			c = s.charAt (i);
			if (c == '_') {
				flag = true;
			} else {
				if (flag) {
					flag = false;
					if (c >= 'A' && c <= 'Z') {
						c += 32;
					}
				} else if (c >= 'A' && c <= 'Z') {
					builder.append ('_');
					c += 32;
				}
			}
			builder.append (c);
		}

		return builder.toString ();
	}
}
