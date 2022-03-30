package com.szw.codegen.core.container.template;


import com.szw.codegen.core.Container;
import com.szw.codegen.core.model.TemplateFile;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 模板源的简单实现
 *
 * @author SZW
 */
@Slf4j
public class SimpleTemplateContainer implements Container<TemplateFile> {
	@Getter
	@Setter
	private String templateDir;
	@Getter
	@Setter
	private List<TemplateFile> templateFiles = new ArrayList<> ();

	private boolean loaded;

	@Override
	public Collection<TemplateFile> getCollection() {
		return loaded ? templateFiles : null;
	}

	@Override
	public void load() {
		loaded = true;

		if (templateFiles != null && templateFiles.size () > 0) {
			templateFiles = this.templateFiles
					.stream ()
					.map (this::loadTemplate)
					.filter (Objects::nonNull)
					.collect (Collectors.toList ());
		} else {
			log.error ("No template were configured,no text file will be generated.");
		}
	}

	private TemplateFile loadTemplate(TemplateFile t) {
		Path path = Paths.get (t.getFilePath ());
		if (!path.isAbsolute ()) {
			path = Paths.get (templateDir, path.toString ());
		}

		log.info ("Load template from path '{}'.", path);

		if (Files.exists (path)) {
			try {
				String content = new String (Files.readAllBytes (path), StandardCharsets.UTF_8);

				if ("".equals (content)) {
					log.warn ("The content of template '{} is empty!'", path);
				}

				t.setContent (content);
				return t;
			} catch (IOException e) {
				log.error ("Failed to read file '{}'!", path);
				e.printStackTrace ();
			}
		} else {
			log.warn ("File '{}' not found, it will not be used.", path);
		}

		return null;
	}
}
