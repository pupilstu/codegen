package com.szw.codegen.core.datasource;

import com.alibaba.fastjson.JSON;
import com.szw.codegen.core.DataSource;
import com.szw.codegen.core.util.NullIterator;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * JSON数据源
 *
 * @author SZW
 */
@Data
@Slf4j
@Accessors(chain = true)
public class JsonDataSource implements DataSource<Object> {
	private String jsonFilePath;
	private String charset = "UTF-8";

	public JsonDataSource() {
		this.jsonFilePath = jsonFilePath;
	}

	public JsonDataSource(String jsonFilePath, String charset) {
		this.jsonFilePath = jsonFilePath;
		this.charset = charset;
	}

	@Override
	public Iterator<Object> iterator() {
		if (jsonFilePath == null || "".equals (jsonFilePath)) {
			log.error ("The jsonFilePath is null, please check your config.");
			return null;
		}

		Path path = Paths.get (jsonFilePath);
		try {
			byte[] bytes = Files.readAllBytes (path);
			return JSON.parseArray (new String (bytes, Charset.forName (charset))).iterator ();
		} catch (IOException e) {
			log.error ("An error occurred in read file '{}'.", jsonFilePath);
			e.printStackTrace ();
		}

		return NullIterator.newInstance ();
	}
}
