package com.szw.codegen.core.model.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SZW
 * @date 2021/8/18
 */
@Data
@Accessors(chain = true)
public class Table {
	/**
	 * String => table name,Upper Camel Case.
	 */
	private final Name name;
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
	private List<Field> fields = new ArrayList<> ();

	public Table(String tableName) {
		this.name = new Name (tableName);
	}
}
