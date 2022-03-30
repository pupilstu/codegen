package com.szw.codegen.core.container.data;

import com.szw.codegen.core.Container;
import lombok.Getter;
import lombok.Setter;
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
public class CsvDataContainer implements Container<Map<String, String>> {
	@Getter
	@Setter
	private String CsvFilePath;
	@Getter
	@Setter
	private String format = "Default";
	@Getter
	@Setter
	private String charset = "UTF-8";

	private List<Map<String, String>> datas;

	@Override
	public Collection<Map<String, String>> getCollection() {
		return datas;
	}

	@Override
	public void load() {
		Path path = Paths.get (CsvFilePath);
		if (Files.exists (path) || !Files.isDirectory (path)) {
			try {
				BufferedReader reader = Files.newBufferedReader (path, Charset.forName (charset));
				CSVParser parser = CSVFormat.valueOf (format).withFirstRecordAsHeader ().parse (reader);

				List<String> headerNames = parser.getHeaderNames ();
				List<CSVRecord> records = parser.getRecords ();
				datas = new ArrayList<> (records.size ());

				for (CSVRecord record : records) {
					Map<String, String> map = new HashMap<> (headerNames.size ());
					for (String headerName : headerNames) {
						map.put (headerName, record.get (headerName));
					}
					datas.add (map);
				}
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}
}
