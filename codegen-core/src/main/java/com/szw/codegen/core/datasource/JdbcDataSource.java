package com.szw.codegen.core.datasource;

import com.szw.codegen.core.DataSource;
import com.szw.codegen.core.entity.Field;
import com.szw.codegen.core.entity.Table;
import com.szw.codegen.core.util.DBUtil;
import com.szw.codegen.core.util.NullIterator;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
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
 * jdbc数据源
 *
 * @author SZW
 */
@Slf4j
public class JdbcDataSource implements DataSource<Table> {
	private final String driver;
	private final String url;
	private final String user;
	private final String password;
	private final List<String> tableNames;
	private final String catalog;
	private final String schemaPattern;
	private final String tableNamePattern;
	private final String columnNamePattern;
	private final Function<Table, Boolean> tableFilter;
	private final Function<Field, Boolean> fieldFilter;

	public JdbcDataSource(Builder builder) {
		driver = builder.driver;
		url = builder.url;
		user = builder.user;
		password = builder.password;
		tableNames = builder.tableNames;
		catalog = builder.catalog;
		schemaPattern = builder.schemaPattern;
		tableNamePattern = builder.tableNamePattern;
		columnNamePattern = builder.columnNamePattern;
		tableFilter = builder.tableFilter;
		fieldFilter = builder.fieldFilter;
	}

	@Override
	public Iterator<Table> iterator() {
		try {
			Connection conn = DBUtil.getConnection (driver, url, user, password);
			//statement
			Statement stmt = conn.createStatement ();
			//获取数据库元数据
			DatabaseMetaData dmd = conn.getMetaData ();

			List<Table> tableList;
			if (tableNames == null || tableNames.size () < 1) {
				log.info ("Didn't find the tableNames config, get table names from db.");

				tableList = DBUtil.getTables (
						stmt, dmd,
						catalog, schemaPattern, tableNamePattern,
						null, columnNamePattern, tableFilter, fieldFilter);
			} else {
				log.info ("Found the tableNames config!");
				tableList = new ArrayList<> (tableNames.size ());

				for (String tableName : tableNames) {
					Table table = DBUtil.getTable (
							stmt, dmd,
							catalog, schemaPattern, tableName,
							null, columnNamePattern, fieldFilter);
					tableList.add (table);
				}
			}

			conn.close ();
			return tableList.iterator ();
		} catch (ClassNotFoundException | SQLException e) {
			log.error ("An error occurred in connecting db and reading db data,please check your 'DBDataConfig'.");
			e.printStackTrace ();
		}
		return NullIterator.newInstance ();
	}

	@SneakyThrows
	public void showHelpInfo() {
		DBUtil.showHelpInfo (DBUtil.getConnection (driver, url, user, password));
	}

	@Data
	@Accessors(chain = true)
	public static class Builder {
		private String driver;
		private String url;
		private String user;
		private String password;
		private List<String> tableNames = new ArrayList<> ();
		private String catalog;
		private String schemaPattern;
		private String tableNamePattern;
		private String columnNamePattern;
		private Function<Table, Boolean> tableFilter;
		private Function<Field, Boolean> fieldFilter;

		public JdbcDataSource build() {
			return new JdbcDataSource (this);
		}
	}
}
