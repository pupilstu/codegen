package com.szw.crudgen;

import com.szw.codegen.core.datasource.CsvDataSource;
import com.szw.codegen.core.datasource.JdbcDataSource;
import com.szw.codegen.core.datasource.JsonDataSource;
import com.szw.codegen.core.receiver.NativeFileReceiver;
import com.szw.codegen.core.templatesource.SimpleTemplateSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * @author SZW
 */
@Data
@ConfigurationProperties(prefix = "crud-gen")
@PropertySource("classpath:application.yml")
public class CrudGenProperties {

	@NestedConfigurationProperty
	private JdbcDataSource.Builder jdbcDs;

	@NestedConfigurationProperty
	private JsonDataSource jsonDs;

	@NestedConfigurationProperty
	private CsvDataSource csvDs;

	@NestedConfigurationProperty
	private SimpleTemplateSource simpleTs;

	@NestedConfigurationProperty
	private NativeFileReceiver nativeFileReceiver;

	private Map<String, Object> staticData;

}
