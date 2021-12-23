package com.szw.codegen.core.datasource;

import com.szw.codegen.core.DataSource;
import com.szw.codegen.core.util.NullIterator;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * csv 数据源
 *
 * @author SZW
 */
@Data
@Accessors(chain = true)
public class CsvDataSource implements DataSource<Map<String, String>> {
	private String CsvFilePath;
	private String format = "Default";
	private String charset = "UTF-8";

	public CsvDataSource(String csvFilePath) {
		CsvFilePath = csvFilePath;
	}

	public CsvDataSource(String csvFilePath, String format, String charset) {
		CsvFilePath = csvFilePath;
		this.format = format;
		this.charset = charset;
	}

	@Override
	public Iterator<Map<String, String>> iterator() {
		Path path = Paths.get (CsvFilePath);
		if (Files.exists (path) || !Files.isDirectory (path)) {
			try {
				BufferedReader reader = Files.newBufferedReader (path, Charset.forName (charset));
				CSVParser parser = CSVFormat.valueOf (format).withFirstRecordAsHeader ().parse (reader);

				List<String> headerNames = parser.getHeaderNames ();
				List<CSVRecord> records = parser.getRecords ();
				List<Map<String, String>> datas = new ArrayList<> (records.size ());

				for (CSVRecord record : records) {
					Map<String, String> map = new HashMap<> (headerNames.size ());
					for (String headerName : headerNames) {
						map.put (headerName, record.get (headerName));
					}
					datas.add (map);
				}
				return datas.iterator ();
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
		return NullIterator.newInstance ();
	}
}
