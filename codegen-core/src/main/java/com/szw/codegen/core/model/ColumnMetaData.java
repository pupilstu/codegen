package com.szw.codegen.core.model;

import com.szw.codegen.core.util.NameUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据库表中的列的元数据。是对{@link java.sql.DatabaseMetaData#getColumns(String, String, String, String)}查询结果的封装。
 *
 * @author SZW
 */
@Data
@Accessors(chain = true)
public class ColumnMetaData {
	/**
	 * String => column name,lower Camel Case.
	 */
	private String name;
	/**
	 * String => raw column name
	 */
	private String rawName;
	/**
	 * String => java class name
	 */
	private String className;
	/**
	 * int => SQL type from java.sql.Types
	 */
	private String dataType;
	/**
	 * String => Data source dependent type name, for a UDT the type name is fully qualified
	 */
	private String typeName;
	/**
	 * int => column size.
	 */
	private String size;
	/**
	 * int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
	 */
	private String decimalDigits;
	/**
	 * int => Radix (typically either 10 or 2)
	 */
	private String numPrecRadix;
	/**
	 * String => comment describing column (may be null)
	 */
	private String remarks;
	/**
	 * String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
	 */
	private String defaultVal;
	/**
	 * int => for char types the maximum number of bytes in the column
	 */
	private String charOctetLength;
	/**
	 * int => index of column in table (starting at 1)
	 */
	private String ordinalPosition;
	/**
	 * String => ISO rules are used to determine the nullability for a column.
	 * YES --- if the column can include NULLs
	 * NO --- if the column cannot include NULLs
	 * empty string --- if the nullability for the column is unknown
	 */
	private String isNullable;
	/**
	 * String => Indicates whether this column is auto incremented
	 * YES --- if the column is auto incremented
	 * NO --- if the column is not auto incremented
	 * empty string --- if it cannot be determined whether the column is auto incremented
	 */
	private String isAutoincrement;

	public ColumnMetaData(String columnName) {
		this.rawName = columnName;
		this.name = NameUtil.toLowerCamelCase (columnName);
	}
}
