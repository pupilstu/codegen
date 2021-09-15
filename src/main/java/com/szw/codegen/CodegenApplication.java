package com.szw.codegen;

import com.szw.codegen.basic.Generator;
import com.szw.codegen.basic.Receiver;
import com.szw.codegen.basic.DataSource;
import com.szw.codegen.core.model.common.TextFile;
import com.szw.codegen.core.templatesource.TextFileTemplateSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.Map;

/**
 * The demo of Codegen.
 *
 * @author SZW
 */
@Slf4j
@SpringBootApplication
public class CodegenApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run (CodegenApplication.class, args);
	}

	@Autowired
	Generator generator;

	@Autowired
	Receiver<TextFile> receiver;

	@Autowired
	TextFileTemplateSource textFileTemplateSource;

	@Autowired
	DataSource dataSource;

	@Resource(name = "additionalConfig")
	Map<String, Object> additionalConfig;

	@Override
	public void run(String... args) {
		Generator generator = new Generator ("data");

		generator.generate (dataSource, textFileTemplateSource, receiver, TextFile.class, null);
	}
}
