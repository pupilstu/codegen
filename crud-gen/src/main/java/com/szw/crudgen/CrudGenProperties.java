package com.szw.crudgen;

import com.szw.codegen.core.container.data.CsvDataContainer;
import com.szw.codegen.core.container.data.JdbcTableMetaDataContainer;
import com.szw.codegen.core.container.data.JsonDataContainer;
import com.szw.codegen.core.container.template.SimpleTemplateContainer;
import com.szw.codegen.core.receiver.NativeFileReceiver;
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
	private JdbcTableMetaDataContainer.Builder jdbcData;

	@NestedConfigurationProperty
	private JsonDataContainer jsonData;

	@NestedConfigurationProperty
	private CsvDataContainer csvData;

	@NestedConfigurationProperty
	private SimpleTemplateContainer template;

	@NestedConfigurationProperty
	private NativeFileReceiver receiver;

	private Map<String, Object> staticData;

}
