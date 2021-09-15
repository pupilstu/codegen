package com.szw.codegen.core.datasource;

import com.alibaba.fastjson.JSON;
import com.szw.codegen.basic.DataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/**
 * @author SZW
 * @date 2021/8/21
 */
@Slf4j
@RequiredArgsConstructor
public class JsonDataSource implements DataSource {

	private final JsonDataSourceConfig config;

	@Override
	public Object getConfig() {
		return config;
	}

	@Override
	public Iterator<java.lang.Object> iterator() {
		List<java.lang.Object> data = getData ();
		return data == null ? NULL_ITERATOR : data.iterator ();
	}

	private List<java.lang.Object> getData() {
		if (config.jsonFilePath == null || "".equals (config.jsonFilePath)) {
			log.error ("The jsonFilePath is null, please check your config.");
			return null;
		}

		Path path = Paths.get (config.jsonFilePath);
		try {
			byte[] bytes = Files.readAllBytes (path);
			return JSON.parseArray (new String (bytes, Charset.forName (config.charset)));
		} catch (IOException e) {
			log.error ("An error occurred in read file '{}'.", config.jsonFilePath);
			e.printStackTrace ();
		}
		return null;
	}


	@Data
	public static class JsonDataSourceConfig {
		private String jsonFilePath;
		private String charset = "UTF-8";
	}
}
