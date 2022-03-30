package com.szw.codegen.core.strategy;

import com.szw.codegen.core.Engine;
import com.szw.codegen.core.MergeStrategy;
import com.szw.codegen.core.model.ResultFile;
import com.szw.codegen.core.model.TemplateFile;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author SZW
 */
@Slf4j
public class TemplateFileMergeStrategy implements MergeStrategy<TemplateFile, ResultFile> {
	@Override
	public ResultFile doMerge(TemplateFile templateFile, Object staticData, Engine engine) {
		if (templateFile.isStaticTemplate ()) {
			return new ResultFile ()
					.setTargetDir (templateFile.getTargetDir ())
					.setFilename (engine.mergeStatic (templateFile.getTargetFilename ()))
					.setContent (engine.mergeStatic (templateFile.getContent ()));
		}

		return null;
	}

	@Override
	public ResultFile doMerge(TemplateFile templateFile, Object data, Object staticData, Engine engine) {
		if (data instanceof Map) {
			Map<?, ?> dataMap = (Map<?, ?>) data;
			return new ResultFile ()
					.setTargetDir (templateFile.getTargetDir ())
					.setFilename (engine.merge (templateFile.getTargetFilename (), dataMap))
					.setContent (engine.merge (templateFile.getContent (), dataMap));
		}

		return new ResultFile ()
				.setTargetDir (templateFile.getTargetDir ())
				.setFilename (engine.merge (templateFile.getTargetFilename (), data))
				.setContent (engine.merge (templateFile.getContent (), data));
	}
}
