package com.szw.codegen.autoconfigure;

import com.szw.codegen.basic.Generator;
import com.szw.codegen.basic.DataSource;
import com.szw.codegen.core.datasource.DbDataSource;
import com.szw.codegen.core.datasource.JsonDataSource;
import com.szw.codegen.core.receiver.TextFileReceiver;
import com.szw.codegen.core.templatesource.TextFileTemplateSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author SZW
 * @date 2021/8/16
 */
@Configuration
@EnableConfigurationProperties(value = {CodegenConfigurationProperties.class})
public class CodegenAutoConfiguration {

	@Autowired
	CodegenConfigurationProperties properties;

	@Bean(name = "receiver")
	TextFileReceiver getReceiver() {
		return new TextFileReceiver (properties.getReceiverConfig ());
	}

	@Bean(name = "templateSource")
	TextFileTemplateSource getTemplateSource() {
		return new TextFileTemplateSource (properties.getTemplateConfig ());
	}

	@Bean(name = "dataSource")
	DataSource getBeanSource() {
		CodegenConfigurationProperties.DataConfigContainer dataConfig = properties.getDataConfig ();
		if (dataConfig.getJson () == null) {
			return new DbDataSource (dataConfig.getDb (), table -> true, field -> true);
		} else {
			return new JsonDataSource (dataConfig.getJson ());
		}
	}

	@Bean(name = "additionalConfig")
	Map<String, Object> getAdditionalConfig() {
		return properties.getAdditionalConfig ();
	}

	@Bean(name = "codeGenerator")
	Generator getCodeGenerator() {
		return new Generator (
				properties.getEngine (),
				properties.getCodeConfigName (),
				properties.getBeanConfigName (),
				properties.getTemplateConfigName (),
				properties.getAdditionalConfigName (),
				properties.getBeanName ());
	}
}
