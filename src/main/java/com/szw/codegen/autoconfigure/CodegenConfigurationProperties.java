package com.szw.codegen.autoconfigure;

import com.szw.codegen.basic.engine.Engine;
import com.szw.codegen.basic.engine.VelocityEngine;
import com.szw.codegen.core.datasource.DbDataSource;
import com.szw.codegen.core.datasource.JsonDataSource;
import com.szw.codegen.core.receiver.TextFileReceiver;
import com.szw.codegen.core.templatesource.TextFileTemplateSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SZW
 * @date 2021/8/26
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "codegen")
@PropertySource(value = "classpath:application.yml")
public class CodegenConfigurationProperties {

	private Engine engine = new VelocityEngine ();

	private String codeConfigName = "cc";
	private String beanConfigName = "bc";
	private String templateConfigName = "tc";
	private String additionalConfigName = "ac";

	private String beanName;

	@NestedConfigurationProperty
	private DataConfigContainer dataConfig;

	@NestedConfigurationProperty
	private TextFileReceiver.TextFileReceiverConfig receiverConfig;

	@NestedConfigurationProperty
	private TextFileTemplateSource.TemplateFileSourceConfig templateConfig;

	private Map<String, Object> additionalConfig = new HashMap<> ();

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(String str) {
		if (str == null || "".equals (str)) {
			return;
		}

		try {
			engine = (Engine) Class.forName (str).getDeclaredConstructor ().newInstance ();
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			try {
				engine = (Engine) Class.forName (str).getDeclaredConstructor ().newInstance ();
			} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException classNotFoundException) {
				log.warn ("Invalid config : codegen.engine, use default engine {}.Please check your config and retry.", VelocityEngine.class.getName ());
			}
		}
	}

	@Data
	public static class DataConfigContainer {
		@NestedConfigurationProperty
		private DbDataSource.DbDataSourceSourceConfig db;

		@NestedConfigurationProperty
		private JsonDataSource.JsonDataSourceConfig json;
	}
}
