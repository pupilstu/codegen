package com.szw.codegen.core.templatesource;


import com.szw.codegen.core.TemplateSource;
import com.szw.codegen.core.entity.Template;
import com.szw.codegen.core.util.NullIterator;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * 模板源的简单实现
 *
 * @author SZW
 */
@Data
@Slf4j
//@Accessors(chain = true)
@NoArgsConstructor
public class SimpleTemplateSource implements TemplateSource {
	private String templateDir;
	private List<Template> templates = new ArrayList<Template> ();

	public SimpleTemplateSource(String templateDir, List<Template> templates) {
		this.templateDir = templateDir;
		this.templates = templates;
	}

	@Override
	public Iterator<Template> iterator() {
		if (templates != null && templates.size () > 0) {
			return templates.stream ()
					.map (t -> {
						Path path = Paths.get (t.getTemplateFilePath ());
						if (!path.isAbsolute ()) {
							path = Paths.get (templateDir, path.toString ());
						}
						if (Files.exists (path)) {
							try {
								String content = new String (Files.readAllBytes (path), StandardCharsets.UTF_8);
								t.setContent (content);
								return t;
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
					.collect (Collectors.toList ())
					.iterator ();
		} else {
			log.error ("No template was configured,no text file will be generated.");
		}
		return NullIterator.newInstance ();
	}
}
