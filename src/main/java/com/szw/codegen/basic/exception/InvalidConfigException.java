package com.szw.codegen.basic.exception;

/**
 * @author SZW
 * @date 2021/8/27
 */
public class InvalidConfigException extends Exception {

	public InvalidConfigException(String occurredIn, String because) {
		super ("The InvalidConfigException occurred in "
		       + occurredIn + ",because " + because + " .Please check your config.");
	}

	public InvalidConfigException(String message) {
		super (message);
	}

	public InvalidConfigException(String message, Throwable cause) {
		super (message, cause);
	}

	public InvalidConfigException(Throwable cause) {
		super (cause);
	}
}
