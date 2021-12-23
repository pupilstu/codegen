package com.szw.crudgen;

import com.szw.codegen.core.Generator;
import com.szw.codegen.core.datasource.JdbcDataSource;
import com.szw.codegen.core.entity.Table;
import com.szw.codegen.core.templatesource.SimpleTemplateSource;
import com.szw.codegen.core.util.NameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author SZW
 */
@Slf4j
@Configuration
@SpringBootApplication
@EnableConfigurationProperties(CrudGenProperties.class)
public class CrudGenApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run (CrudGenApplication.class, args);
	}

	@Autowired
	private CrudGenProperties crudGenProperties;

	@Override
	public void run(String... args) throws Exception {
		JdbcDataSource.Builder builder = crudGenProperties.getJdbcDs ();

		/**
		 * You can write your own filters here.
		 */
		builder.setTableFilter (table -> true);
		builder.setFieldFilter (field -> true);

		JdbcDataSource dataSource = builder.build ();

		/**
		 * 显示帮助信息，帮助信息会指示catalog和scheam哪一个是真正的数据库名，注意查看日志打印。
		 */
		dataSource.showHelpInfo ();

		SimpleTemplateSource simpleTs = crudGenProperties.getSimpleTs ();

		Generator generator = new Generator.Builder ()
				.setDataSource (dataSource)
				.setTemplateSource (simpleTs)
				.setStaticData (crudGenProperties.getStaticData ())
				.addInterceptors (chain -> {
					Object data = chain.getData ();

					Table table = (Table) data;
					HashMap<String, Object> map = new HashMap<> ();
					map.put ("className", table.getName ());
					map.put ("classname", table.getLccName ());
					map.put ("tableName", NameUtil.toUnderScoreCase (table.getName ()));
					map.put ("pathName", NameUtil.toKebabCase (table.getName ()));
					map.put ("comments", table.getRemarks ());
					map.put ("columns", table.getFields ());

					return chain.proceed (map, chain.getTemplate ());

				})
				.setReceiver (crudGenProperties.getNativeFileReceiver ())
				.build ();

		generator.generate ();
	}
}
