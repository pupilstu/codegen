package com.szw.codegen.core.util;

import com.szw.codegen.core.model.ColumnMetaData;
import com.szw.codegen.core.model.TableMetaData;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 数据库元数据工具类，用于解析元数据，将元数据信息封装成{@link TableMetaData}和{@link ColumnMetaData}。
 *
 * @author SZW
 */
@Slf4j
public class DBMetaDataUtil implements AutoCloseable {

	/**
	 * 数据库连接信息
	 */
	private final String driver;
	private final String url;
	private final String user;
	private final String password;

	private final Connection connection;
	private final Statement statement;
	private final DatabaseMetaData dbMetaData;

	public DBMetaDataUtil(String driver, String url, String user, String password) throws SQLException, ClassNotFoundException {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;

		this.connection = getConnection ();
		this.statement = connection.createStatement ();
		this.dbMetaData = connection.getMetaData ();
	}

	/**
	 * 获取数据库连接
	 *
	 * @return 数据库连接
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName (driver);
		return DriverManager.getConnection (url, user, password);
	}

	/**
	 * 将元数据转化为{@link TableMetaData}对象
	 */
	protected TableMetaData toTableMetaData(ResultSet resultSet) throws SQLException {
		String tableCat = resultSet.getString ("TABLE_CAT");
		String tableSchem = resultSet.getString ("TABLE_SCHEM");
		String tableName = resultSet.getString ("TABLE_NAME");
		String tableType = resultSet.getString ("TABLE_TYPE");
		String remarks = resultSet.getString ("REMARKS");

		/* 其他元数据：
		 * String selfReferencingColName = resultSet.getString ("SELF_REFERENCING_COL_NAME");
		 * String refGeneration = resultSet.getString ("REF_GENERATION");
		 */

		return new TableMetaData (tableName)
				.setCatalog (tableCat)
				.setSchema (tableSchem)
				.setType (tableType)
				.setRemarks (remarks);
	}

	/**
	 * 获取数据库指定表的{@link TableMetaData}对象
	 *
	 * @param catalog           目录
	 * @param schema            模式
	 * @param tableName         表名
	 * @param types             表类型
	 * @param columnNamePattern 列名模式
	 * @param columnFilter      字段过滤器
	 * @return TableMetaData
	 */
	public TableMetaData getTableMetaData(
			String catalog, String schema, String tableName, String[] types,
			String columnNamePattern, Function<ColumnMetaData, Boolean> columnFilter) throws SQLException {

		ResultSet resultSet = dbMetaData.getTables (catalog, schema, tableName, types);

		if (!resultSet.next ()) {
			return null;
		}

		TableMetaData tableMetaData = toTableMetaData (resultSet);

		return tableMetaData.setColumnMetaDatas (getColumnMetaDatas (
				tableMetaData.getCatalog (),
				tableMetaData.getSchema (),
				tableMetaData.getRawName (),
				columnNamePattern, columnFilter));
	}

	/**
	 * 获取符合条件的全部表。过滤器用在模式之后。
	 *
	 * @param catalog           目录
	 * @param schemaPattern     schema模式
	 * @param tableNamePattern  表名模式
	 * @param types             表类型，null表示全部
	 * @param columnNamePattern 列名模式
	 * @param tableFilter       表过滤器
	 * @param columnFilter      字段过滤器
	 * @return 表列表
	 */
	public List<TableMetaData> getTableMetaDatas(
			String catalog, String schemaPattern, String tableNamePattern, String[] types,
			String columnNamePattern, Function<TableMetaData, Boolean> tableFilter,
			Function<ColumnMetaData, Boolean> columnFilter) throws SQLException {

		//Get resultSet.
		ResultSet resultSet = dbMetaData.getTables (catalog, schemaPattern, tableNamePattern, types);

		List<TableMetaData> tableMetaDataList = new ArrayList<> ();

		while (resultSet.next ()) {
			TableMetaData tableMetaData = toTableMetaData (resultSet);

			if (tableFilter.apply (tableMetaData)) {
				List<ColumnMetaData> columnMetaDatas = getColumnMetaDatas (
						tableMetaData.getCatalog (),
						tableMetaData.getSchema (),
						tableMetaData.getRawName (),
						columnNamePattern, columnFilter);

				tableMetaData.setColumnMetaDatas (columnMetaDatas);
				tableMetaDataList.add (tableMetaData);
			}
		}
		return tableMetaDataList;
	}

	/**
	 * 获取某个表的全部字段
	 *
	 * @param catalog           目录
	 * @param schema            schema
	 * @param tableName         表名
	 * @param columnNamePattern 列名模式
	 * @param fieldFilter       字段过滤器
	 * @return 字段列表
	 */
	public List<ColumnMetaData> getColumnMetaDatas(
			String catalog,
			String schema,
			String tableName,
			String columnNamePattern,
			Function<ColumnMetaData, Boolean> fieldFilter) throws SQLException {

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
		ResultSet rs = statement.executeQuery ("SELECT * FROM " + table + " where 0=1");
		ResultSetMetaData rmd = rs.getMetaData ();

		int columnCount = rmd.getColumnCount ();
		Map<String, String> map = new HashMap<> (columnCount);
		for (int i = 1; i < columnCount + 1; i++) {
			map.put (rmd.getColumnName (i), rmd.getColumnClassName (i));
		}

		//获取列信息
		ResultSet columns = dbMetaData.getColumns (catalog, schema, tableName, columnNamePattern);

		List<ColumnMetaData> columnMetaDataList = new ArrayList<> (columnCount);
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
			ColumnMetaData columnMetaData = new ColumnMetaData (columnName)
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

			if (fieldFilter.apply (columnMetaData)) {
				columnMetaDataList.add (columnMetaData);
			}
		}
		return columnMetaDataList;
	}

	/**
	 * 关于数据库catalog和schema的帮助信息
	 */
	public void showHelpInfo() throws SQLException {
		log.info ("Your database product name is: {}", dbMetaData.getDatabaseProductName ());
		log.info ("Your database product version is: {}", dbMetaData.getDatabaseProductVersion ());
		log.info ("The 'catalog' indicates '{}' in your database.", dbMetaData.getCatalogTerm ());
		log.info ("The 'schema' indicates '{}' in your database.", dbMetaData.getSchemaTerm ());
	}

	@Override
	public void close() throws Exception {
		connection.close ();
	}
}
