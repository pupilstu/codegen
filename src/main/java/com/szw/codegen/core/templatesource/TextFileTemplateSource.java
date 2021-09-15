package com.szw.codegen.core.templatesource;

import com.szw.codegen.basic.TemplateSource;
import com.szw.codegen.core.model.common.TextFile;
import com.szw.codegen.core.model.template.TextFileTemplate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author SZW
 * @date 2021/8/21
 */
@Slf4j
@RequiredArgsConstructor
public class TextFileTemplateSource implements TemplateSource<TextFile> {

	private final TemplateFileSourceConfig config;

	@Override
	public Object getConfig() {
		return config;
	}

	@Override
	public Iterator<TextFile> iterator() {
		// 如果没有手动配置模板文件则使用模板文件夹下的文件作为模板
		if (config.templates != null && config.templates.size () > 0) {
			return config.templates.stream ()
					.map (t -> {
						Path path = Paths.get (t.getTemplateFilePath ());
						if (!path.isAbsolute ()) {
							path = Paths.get (config.templateDir, path.toString ());
						}
						if (Files.exists (path)) {
							try {
								String content = new String (Files.readAllBytes (path), StandardCharsets.UTF_8);
								return new TextFile ()
										.setFilename (t.getFilenameTemplate ())
										.setContent (content)
										.setParentDir (t.getTargetDir ());
							} catch (IOException e) {
								log.warn ("FilePropItem '{}' not found,it will not be used.", path);
								e.printStackTrace ();
							}
						} else {
							log.warn (" '{}' not found,it will not be used.", path);
						}
						return null;
					})
					.filter (Objects::nonNull)
					.collect (Collectors.toList ()).iterator ();
		} else {
			log.warn ("No template was configured,no text file will be generated.");
		}
		return null;
	}


	@Data
	public static class TemplateFileSourceConfig {
		private String templateDir;
		private String suffix;
		private List<TextFileTemplate> templates = new ArrayList<> ();
	}
}
