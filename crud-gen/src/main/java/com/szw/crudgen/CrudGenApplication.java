package com.szw.crudgen;

import com.szw.codegen.core.Generator;
import com.szw.codegen.core.container.data.JdbcTableMetaDataContainer;
import com.szw.codegen.core.container.template.SimpleTemplateContainer;
import com.szw.codegen.core.interceptor.DataMapInterceptor;
import com.szw.codegen.core.interceptor.TypeMapInterceptor;
import com.szw.codegen.core.model.ColumnMetaData;
import com.szw.codegen.core.model.ResultFile;
import com.szw.codegen.core.model.TableMetaData;
import com.szw.codegen.core.model.TemplateFile;
import com.szw.codegen.core.strategy.TemplateFileMergeStrategy;
import com.szw.codegen.core.util.NameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Map;

/**
 * @author SZW
 */
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(CrudGenProperties.class)
public class CrudGenApplication implements CommandLineRunner, DataMapInterceptor.Mapper<TableMetaData> {

	public CrudGenApplication(CrudGenProperties crudGenProperties) {
		this.crudGenProperties = crudGenProperties;
	}

	public static void main(String[] args) {
		SpringApplication.run (CrudGenApplication.class, args);
	}


	@Override
	public void doMapping(TableMetaData rawData, Map<String, Object> dataMap) {
		dataMap.put ("className", rawData.getName ());
		dataMap.put ("classname", rawData.getLccName ());
		dataMap.put ("tableName", NameUtil.toUnderScoreCase (rawData.getName ()));
		dataMap.put ("pathName", NameUtil.toKebabCase (rawData.getName ()));
		dataMap.put ("comments", rawData.getRemarks ());
		dataMap.put ("columns", rawData.getColumnMetaDatas ());

		for (ColumnMetaData columnMetaData : rawData.getColumnMetaDatas ()) {
			String trim = columnMetaData.getClassName ().trim ();

			switch (trim) {
				case "BigDecimal":
					dataMap.put ("hasBigDecimal", true);
					break;
				case "LocalDate":
					dataMap.put ("hasLocalDate", true);
					break;
				case "LocalDateTime":
					dataMap.put ("hasLocalDateTime", true);
					break;
				default:
					break;
			}
		}
	}

	private final CrudGenProperties crudGenProperties;

	@Override
	public void run(String... args) {
		JdbcTableMetaDataContainer.Builder builder = crudGenProperties.getJdbcData ();

		/*
		 * You can write your own filters here.
		 */
		builder.setTableFilter (table -> true);
		builder.setFieldFilter (field -> true);

		JdbcTableMetaDataContainer dataContainer = builder.build ();

		/*
		 * 显示帮助信息，帮助信息会指示catalog和scheam哪一个是真正的数据库名，注意查看日志打印。
		 */
		dataContainer.showHelpInfo ();

		SimpleTemplateContainer templateContainer = crudGenProperties.getTemplate ();

		Generator.<TemplateFile, ResultFile>executor ()
				.setTemplates (templateContainer)
				.setDatas (dataContainer)
				.setStaticData (crudGenProperties.getStaticData ())
				.setReceiver (crudGenProperties.getReceiver ())
				.setMergeStrategy (new TemplateFileMergeStrategy ())
				.addInterceptors ()
				.addNext (new TypeMapInterceptor ("typeMapping.properties"))
				.addNext (new DataMapInterceptor<> (this, TableMetaData.class))
				.finished ()
				.execute ();
	}
}
