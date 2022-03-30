package com.szw.codegen.core.container.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.szw.codegen.core.Container;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * JSON数据源
 *
 * @author SZW
 */
@Slf4j
public class JsonDataContainer implements Container<Object> {
	@Getter
	@Setter
	private String jsonFilePath;

	@Getter
	@Setter
	private String charset = "UTF-8";

	private JSONArray objects;

	@Override
	public Collection<Object> getCollection() {
		return objects;
	}

	@Override
	public void load() {
		if (jsonFilePath == null || "".equals (jsonFilePath)) {
			log.error ("The jsonFilePath is null, please check your config.");
		}

		Path path = Paths.get (jsonFilePath);
		try {
			byte[] bytes = Files.readAllBytes (path);
			objects = JSON.parseArray (new String (bytes, Charset.forName (charset)));
		} catch (IOException e) {
			log.error ("An error occurred in read file '{}'.", jsonFilePath);
			e.printStackTrace ();
		}
	}
}
