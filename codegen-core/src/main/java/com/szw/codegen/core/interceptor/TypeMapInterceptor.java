package com.szw.codegen.core.interceptor;

import com.szw.codegen.core.Interceptor;
import com.szw.codegen.core.model.ResultFile;
import com.szw.codegen.core.model.TableMetaData;
import com.szw.codegen.core.model.TemplateFile;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * @author SZW
 */
public class TypeMapInterceptor implements Interceptor<TemplateFile, ResultFile> {
	public static final String DEFAULT_PROPERTIES_FILE_NAME = "typeMapping.properties";

	private final Map<String, String> typeMap;

	public TypeMapInterceptor(Map<String, String> typeMap) {
		this.typeMap = typeMap;
	}

	public TypeMapInterceptor(String propertiesFileName) {
		try {
			PropertiesConfiguration configuration = new PropertiesConfiguration (propertiesFileName);

			typeMap = new HashMap<> ();
			Iterator<String> keys = configuration.getKeys ();

			while (keys.hasNext ()) {
				String key = keys.next ();
				typeMap.put (key, configuration.getString (key));
			}

		} catch (ConfigurationException e) {
			throw new RuntimeException ("Failed to read file '" + propertiesFileName + "'.");
		}
	}

	public TypeMapInterceptor() {
		this (DEFAULT_PROPERTIES_FILE_NAME);
	}

	@Override
	public ResultFile intercept(TemplateFile template, Object data, Chain<TemplateFile, ResultFile> chain) {

		if (data instanceof TableMetaData) {
			TableMetaData tableMetaData = (TableMetaData) data;

			tableMetaData.getColumnMetaDatas ().forEach (
					field -> {
						String className = typeMap.get (field.getTypeName ().toLowerCase (Locale.ROOT));
						if (className != null) {
							field.setClassName (className);
						}
					}
			);
		}

		return chain.proceed (template, data);
	}
}