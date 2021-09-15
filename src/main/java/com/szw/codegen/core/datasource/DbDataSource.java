package com.szw.codegen.core.datasource;

import com.szw.codegen.basic.DataSource;
import com.szw.codegen.core.model.data.Field;
import com.szw.codegen.core.model.data.Table;
import com.szw.codegen.core.util.DBUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;


/**
 * @author SZW
 * @date 2021/8/22
 */
@Slf4j
@RequiredArgsConstructor
public class DbDataSource implements DataSource {

	public static final List<java.lang.Object> NULL_LIST = new ArrayList<> (0);

	private final DbDataSourceSourceConfig config;

	private final Function<Table, Boolean> tableFilter;

	private final Function<Field, Boolean> fieldFilter;

	@Override
	public Object getConfig() {
		return config;
	}

	private List<java.lang.Object> tables = null;

	@Override
	public Iterator<java.lang.Object> iterator() {
		if (tables == null) {
			tables = getTables ();
		}
		return tables.iterator ();
	}

	private List<java.lang.Object> getTables() {
		try {
			Connection conn = DBUtil.getConnection (config.driver, config.url, config.user, config.password);
			//statement
			Statement stmt = conn.createStatement ();
			//获取数据库元数据
			DatabaseMetaData dmd = conn.getMetaData ();

			List<java.lang.Object> tableList;
			if (config.tableNames == null || config.tableNames.size () < 1) {
				log.info ("Didn't find the tableNames config, get table names from db.");
				tableList = new ArrayList<> (DBUtil.getTables (stmt, dmd,
						config.catalog, config.schemaPattern, config.tableNamePattern, null, config.columnNamePattern, tableFilter, fieldFilter));
			} else {
				log.info ("Found the tableNames config!");
				tableList = new ArrayList<> (config.tableNames.size ());
				for (String tableName : config.tableNames) {
					tableList.add (DBUtil.getTable (stmt, dmd, config.catalog, config.schemaPattern, tableName, null, config.columnNamePattern, fieldFilter));
				}
			}

			conn.close ();
			return tableList;
		} catch (ClassNotFoundException | SQLException e) {
			log.error ("An error occurred in connecting db and reading db data,please check your 'DBDataConfig'.");
			e.printStackTrace ();
		}
		return NULL_LIST;
	}


	@Data
	public static class DbDataSourceSourceConfig {
		String driver;
		String url;
		String user;
		String password;
		List<String> tableNames = new ArrayList<> ();
		String catalog;
		String schemaPattern;
		String tableNamePattern;
		String columnNamePattern;
	}
}
