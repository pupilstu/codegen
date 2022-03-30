package com.szw.codegen.core.model;

import com.szw.codegen.core.util.NameUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表元数据。是对{@link DatabaseMetaData#getTables(String, String, String, String[])}查询结果的封装。
 *
 * @author SZW
 */
@Slf4j
@Data
@Accessors(chain = true)
public class TableMetaData {
	/**
	 * String => table name,Upper Camel Case.
	 */
	private String name;
	/**
	 * String => table name,lower Camel Case.
	 */
	private String lccName;
	/**
	 * String => raw table name.
	 */
	private String rawName;
	/**
	 * String => table catalog (may be null)
	 */
	private String catalog;
	/**
	 * String => table schema (may be null)
	 */
	private String schema;
	/**
	 * String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	 */
	private String type;
	/**
	 * String => explanatory comment on the table
	 */
	private String remarks;
	/**
	 * Fields
	 */
	private List<ColumnMetaData> columnMetaDatas = new ArrayList<> ();

	public TableMetaData(String tableName) {
		this.rawName = tableName;
		this.lccName = NameUtil.toLowerCamelCase (tableName);
		this.name = NameUtil.toUpperCamelCase (tableName);
	}
}
