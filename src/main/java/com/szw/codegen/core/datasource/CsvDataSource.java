package com.szw.codegen.core.datasource;

import com.szw.codegen.basic.DataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
 * @author SZW
 * @date 2021/9/13
 */
@RequiredArgsConstructor
public class CsvDataSource implements DataSource {

	private final CsvDataSourceConfig config;

	@Override
	public Object getConfig() {
		return config;
	}

	@Override
	public Iterator<Object> iterator() {
		Path path = Paths.get (config.CsvFilePath);
		if (Files.exists (path) || !Files.isDirectory (path)) {
			try {
				BufferedReader reader = Files.newBufferedReader (path, Charset.forName (config.charset));
				CSVParser parser = CSVFormat.valueOf (config.format).withFirstRecordAsHeader ().parse (reader);

				List<String> headerNames = parser.getHeaderNames ();
				List<CSVRecord> records = parser.getRecords ();
				List<Object> data = new ArrayList<> (records.size ());

				for (CSVRecord record : records) {
					Map<String, String> map = new HashMap<> (headerNames.size ());
					for (String headerName : headerNames) {
						map.put (headerName, record.get (headerName));
					}
					data.add (map);
				}
				return data.iterator ();
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
		return null;
	}


	@Data
	public static class CsvDataSourceConfig {
		private String CsvFilePath;
		private String format = "Default";
		private String charset = "UTF-8";
	}
}
