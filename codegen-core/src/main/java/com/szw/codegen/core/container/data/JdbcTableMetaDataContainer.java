package com.szw.codegen.core.container.data;

import com.szw.codegen.core.Container;
import com.szw.codegen.core.model.ColumnMetaData;
import com.szw.codegen.core.model.TableMetaData;
import com.szw.codegen.core.util.DBMetaDataUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;


/**
 * jdbc 表元数据容器
 *
 * @author SZW
 */
@Slf4j
public class JdbcTableMetaDataContainer implements Container<TableMetaData> {
	private final String driver;
	private final String url;
	private final String user;
	private final String password;
	private final List<String> tableNames;
	private final String catalog;
	private final String schemaPattern;
	private final String tableNamePattern;
	private final String columnNamePattern;
	private final Function<TableMetaData, Boolean> tableFilter;
	private final Function<ColumnMetaData, Boolean> fieldFilter;

	private List<TableMetaData> tableMetaDataList;

	private JdbcTableMetaDataContainer(Builder builder) {
		this.driver = builder.driver;
		this.url = builder.url;
		this.user = builder.user;
		this.password = builder.password;
		this.tableNames = builder.tableNames;
		this.catalog = builder.catalog;
		this.schemaPattern = builder.schemaPattern;
		this.tableNamePattern = builder.tableNamePattern;
		this.columnNamePattern = builder.columnNamePattern;
		this.tableFilter = builder.tableFilter;
		this.fieldFilter = builder.fieldFilter;
	}

	@Override
	public Collection<TableMetaData> getCollection() {
		return tableMetaDataList;
	}

	@Override
	public void load() {
		DBMetaDataUtil dbMetaDataUtil = null;

		try {
			dbMetaDataUtil = new DBMetaDataUtil (driver, url, user, password);

			if (tableNames == null || tableNames.size () < 1) {
				log.info ("Didn't find the tableNames config," +
				          " get table names from db by using 'tableNamePattern'.");

				tableMetaDataList = dbMetaDataUtil.getTableMetaDatas (
						catalog, schemaPattern, tableNamePattern, null,
						columnNamePattern, tableFilter, fieldFilter);
			} else {
				log.info ("Found the tableNames config!");

				tableMetaDataList = new ArrayList<> (tableNames.size ());

				for (String tableName : tableNames) {
					TableMetaData tableMetaData = dbMetaDataUtil.getTableMetaData (
							catalog, schemaPattern, tableName,
							null, columnNamePattern, fieldFilter);

					if (Objects.nonNull (tableMetaData)) {
						tableMetaDataList.add (tableMetaData);
					}
				}
			}

			// 打印日志，提示将被使使用的表
			if (log.isInfoEnabled ()) {
				StringBuilder tables = new StringBuilder ();
				for (TableMetaData tableMetaData : tableMetaDataList) {
					tables.append (tableMetaData.getRawName ()).append (", ");
				}
				log.info ("These tables wil be used: {}", tables);
			}


		} catch (ClassNotFoundException | SQLException e) {
			log.error ("An error occurred in connecting db and reading db data,please check your 'DBDataConfig'.");
			e.printStackTrace ();
		} finally {
			if (dbMetaDataUtil != null) {
				try {
					dbMetaDataUtil.close ();
				} catch (Exception e) {
					e.printStackTrace ();
				}
			}
		}
	}

	public void showHelpInfo() {
		DBMetaDataUtil dbMetaDataUtil = null;

		try {
			dbMetaDataUtil = new DBMetaDataUtil (driver, url, user, password);
			dbMetaDataUtil.showHelpInfo ();
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			if (dbMetaDataUtil != null) {
				try {
					dbMetaDataUtil.close ();
				} catch (Exception e) {
					e.printStackTrace ();
				}
			}
		}
	}

	@Data
	@Slf4j
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
		private Function<TableMetaData, Boolean> tableFilter = tableMetaData -> true;
		private Function<ColumnMetaData, Boolean> fieldFilter = columnMetaData -> true;

		public JdbcTableMetaDataContainer build() {
			return new JdbcTableMetaDataContainer (this);
		}
	}

}
