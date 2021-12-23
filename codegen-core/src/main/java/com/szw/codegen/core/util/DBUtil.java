package com.szw.codegen.core.util;

import com.szw.codegen.core.entity.Field;
import com.szw.codegen.core.entity.Table;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 数据库工具类，用于创建数据库连接、解析元数据等
 *
 * @author SZW
 */
@Slf4j
public class DBUtil {

	/**
	 * 获取数据库连接
	 *
	 * @param driver   驱动
	 * @param url      url
	 * @param user     用户
	 * @param password 密码
	 * @return 数据库连接
	 */
	public static Connection getConnection(String driver, String url, String user, String password) throws ClassNotFoundException, SQLException {
		Class.forName (driver);
		return DriverManager.getConnection (url, user, password);
	}

	/**
	 * 将元数据转化为{@link Table}对象
	 */
	private static Table toTable(ResultSet resultSet) throws SQLException {
		String tableCat = resultSet.getString ("TABLE_CAT");
		String tableSchem = resultSet.getString ("TABLE_SCHEM");
		String tableName = resultSet.getString ("TABLE_NAME");
		String tableType = resultSet.getString ("TABLE_TYPE");
		String remarks = resultSet.getString ("REMARKS");

		/* 其他元数据：
		 * String selfReferencingColName = resultSet.getString ("SELF_REFERENCING_COL_NAME");
		 * String refGeneration = resultSet.getString ("REF_GENERATION");*/

		return new Table (tableName)
				.setCatalog (tableCat)
				.setSchema (tableSchem)
				.setType (tableType)
				.setRemarks (remarks);
	}

	/**
	 * 获取数据库指定表的{@link Table}对象
	 *
	 * @param stmt              Statement
	 * @param dbMetaData        元数据对象
	 * @param catalog           目录
	 * @param schema            模式
	 * @param tableName         表名
	 * @param type              表类型
	 * @param columnNamePattern 列名模式
	 * @param fieldFilter       表过滤器
	 * @return Table
	 */
	public static Table getTable(Statement stmt, DatabaseMetaData dbMetaData,
	                             String catalog,
	                             String schema,
	                             String tableName,
	                             String type,
	                             String columnNamePattern,
	                             Function<Field, Boolean> fieldFilter) throws SQLException {

		ResultSet resultSet = dbMetaData.getTables (catalog, schema, tableName, new String[]{type});

		if (!resultSet.next ()) {
			return null;
		}

		Table table = toTable (resultSet);

		return table.setFields (getFields (
				stmt, dbMetaData,
				table.getCatalog (),
				table.getSchema (),
				table.getRawName (),
				columnNamePattern, fieldFilter));
	}

	/**
	 * 获取符合条件的全部表
	 *
	 * @param stmt              Statement
	 * @param dbMetaData        元数据对象
	 * @param catalog           目录
	 * @param schemaPattern     schema模式
	 * @param tableNamePattern  表名
	 * @param types             表类型
	 * @param columnNamePattern 列名模式
	 * @param tableFilter       表过滤器
	 * @param fieldFilter       字段过滤器
	 * @return 表列表
	 */
	public static List<Table> getTables(Statement stmt, DatabaseMetaData dbMetaData,
	                                    String catalog,
	                                    String schemaPattern,
	                                    String tableNamePattern,
	                                    String[] types,
	                                    String columnNamePattern,
	                                    Function<Table, Boolean> tableFilter,
	                                    Function<Field, Boolean> fieldFilter) throws SQLException {
		//Get resultSet.
		ResultSet resultSet = dbMetaData.getTables (catalog, schemaPattern, tableNamePattern, types);

		List<Table> list = new ArrayList<> ();

		while (resultSet.next ()) {
			Table table = toTable (resultSet);

			if (tableFilter.apply (table)) {
				list.add (table.setFields (getFields (
						stmt, dbMetaData,
						table.getCatalog (),
						table.getSchema (),
						table.getRawName (),
						columnNamePattern, fieldFilter)));
			}
		}
		return list;
	}

	/**
	 * 获取某个表的全部字段
	 *
	 * @param stmt              Statement
	 * @param dbMetaData        元数据对象
	 * @param catalog           目录
	 * @param schema            schema
	 * @param tableName         表名
	 * @param columnNamePattern 列名模式
	 * @param fieldFilter       字段过滤器
	 * @return 字段列表
	 */
	public static List<Field> getFields(Statement stmt, DatabaseMetaData dbMetaData,
	                                    String catalog,
	                                    String schema,
	                                    String tableName,
	                                    String columnNamePattern,
	                                    Function<Field, Boolean> fieldFilter) throws SQLException {
		catalog = "".equals (catalog) ? null : catalog;
		schema = "".equals (schema) ? null : schema;

		String table = "`";
		if (catalog != null) {
			table += catalog + "`.`";
		}
		if (schema != null) {
			table += schema + "`.`";
		}
		table += tableName + "`";

		//查询并从查询结果获取元数据
		ResultSet rs = stmt.executeQuery ("SELECT * FROM " + table + " where 0=1");
		ResultSetMetaData rmd = rs.getMetaData ();

		int columnCount = rmd.getColumnCount ();
		Map<String, String> map = new HashMap<> (columnCount);
		for (int i = 1; i < columnCount + 1; i++) {
			map.put (rmd.getColumnName (i), rmd.getColumnClassName (i));
		}

		//获取列信息
		ResultSet columns = dbMetaData.getColumns (catalog, schema, tableName, columnNamePattern);

		List<Field> fields = new ArrayList<> (columnCount);
		while (columns.next ()) {
			String columnName = columns.getString ("COLUMN_NAME");
			String dataType = columns.getString ("DATA_TYPE");
			String typeName = columns.getString ("TYPE_NAME");
			String columnSize = columns.getString ("COLUMN_SIZE");
			String decimalDigits = columns.getString ("DECIMAL_DIGITS");
			String numPrecRadix = columns.getString ("NUM_PREC_RADIX");
			String remarks = columns.getString ("REMARKS");
			String columnDef = columns.getString ("COLUMN_DEF");
			String charOctetLength = columns.getString ("CHAR_OCTET_LENGTH");
			String ordinalPosition = columns.getString ("ORDINAL_POSITION");
			String isNullable = columns.getString ("IS_NULLABLE");
			String isAutoincrement = columns.getString ("IS_AUTOINCREMENT");

			/* 其他元数据：
			 * String scopeCatalog = columns.getString ("SCOPE_CATALOG");
			 * String scopeSchema = columns.getString ("SCOPE_SCHEMA");
			 * String scopeTable = columns.getString ("SCOPE_TABLE");
			 * String sourceDataType = columns.getString ("SOURCE_DATA_TYPE");*/

			String className = map.get (columnName);
			Field field = new Field (columnName)
					.setClassName (className)
					.setDataType (dataType)
					.setTypeName (typeName)
					.setSize (columnSize)
					.setDecimalDigits (decimalDigits)
					.setNumPrecRadix (numPrecRadix)
					.setRemarks (remarks)
					.setDefaultVal (columnDef)
					.setCharOctetLength (charOctetLength)
					.setOrdinalPosition (ordinalPosition)
					.setIsNullable (isNullable)
					.setIsAutoincrement (isAutoincrement);

			if (fieldFilter.apply (field)) {
				fields.add (field);
			}
		}
		return fields;
	}

	/**
	 * 关于数据库catalog和schema的帮助信息
	 *
	 * @param conn 数据库连接
	 */
	public static void showHelpInfo(Connection conn) {
		try {
			DatabaseMetaData metaData = conn.getMetaData ();
			log.info ("Your database product name is: {}", metaData.getDatabaseProductName ());
			log.info ("Your database product version is: {}", metaData.getDatabaseProductVersion ());
			log.info ("The 'catalog' indicates '{}' in your database.", metaData.getCatalogTerm ());
			log.info ("The 'schema' indicates '{}' in your database.", metaData.getSchemaTerm ());
		} catch (SQLException e) {
			e.printStackTrace ();
		}
	}
}
