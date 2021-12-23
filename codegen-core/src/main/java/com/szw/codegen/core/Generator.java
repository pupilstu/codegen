package com.szw.codegen.core;

import com.szw.codegen.core.engine.VelocityTemplateEngine;
import com.szw.codegen.core.entity.Code;
import com.szw.codegen.core.entity.Template;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author SZW
 */
@Slf4j
@Getter
public class Generator {
	private final Engine engine;
	private final DataSource<?> dataSource;
	private final TemplateSource templateSource;
	private final Receiver receiver;
	private final List<Interceptor> interceptorList;
	private final Object staticData;
	private final String staticDataName;
	private final String dataName;

	private final Builder builder;

	public Generator(Builder builder) {
		this.engine = builder.engine;
		this.dataSource = builder.dataSource;
		this.staticData = builder.staticData;
		this.templateSource = builder.templateSource;
		this.receiver = builder.receiver;
		this.interceptorList = builder.interceptorList;
		this.dataName = builder.dataName;
		this.staticDataName = builder.staticDataName;
		this.builder = builder;

		interceptorList.add (new MergeInterceptor ());
	}

	/**
	 * 生成，数据源和模板源提供数据和模板，由接收器完成下一步工作（例如将结果写入文件）
	 */
	public void generate() {
		//NotNull check.
		Iterator<?> dataItr = dataSource.iterator ();
		if (!dataItr.hasNext ()) {
			log.warn ("No Beans available, will generate nothing.");
			return;
		}
		Iterator<Template> templateItr = templateSource.iterator ();
		if (!templateItr.hasNext ()) {
			log.warn ("No template available, will generate nothing.");
			return;
		}

		// add static data
		if (staticData instanceof Map) {
			engine.addStaticData ((Map<?, ?>) staticData);
		} else {
			engine.addStaticData (staticDataName, staticData);
		}

		while (templateItr.hasNext ()) {
			Template template = templateItr.next ();

			/*
			 * 静态模板
			 */
			Boolean staticScope = template.getStaticScope ();
			if (staticScope != null && staticScope) {
				Iterator<Interceptor> interceptorItr = interceptorList.iterator ();

				ChainImpl chain = new ChainImpl (null, template, this.builder, interceptorItr);

				Code code = interceptorItr.next ().intercept (chain);

				receiver.receive (code);
			}

			/*
			 * 非静态模板，遍历data，生成代码
			 */
			while (dataItr.hasNext ()) {
				Object data = dataItr.next ();

				Iterator<Interceptor> interceptorItr = interceptorList.iterator ();

				ChainImpl chain = new ChainImpl (data, template, this.builder, interceptorItr);

				Code code = interceptorItr.next ().intercept (chain);

				receiver.receive (code);
			}

			// 重置遍历器
			dataItr = dataSource.iterator ();
		}
	}

	/**
	 * 拦截链上的最后一个拦截器，执行模板域数据合并的任务
	 */
	private class MergeInterceptor implements Interceptor {
		@Override
		public Code intercept(Chain chain) {
			Object data = chain.getData ();
			Template template = chain.getTemplate ();
			Code code;

			if (data == null) {
				code = new Code ()
						.setParentDir (template.getTargetDir ())
						.setFilename (engine.mergeStatic (template.getFilenameTemplate ()))
						.setContent (engine.mergeStatic (template.getContent ()));
				return code;
			}

			if (data instanceof Map) {
				Map<?, ?> dataMap = (Map<?, ?>) data;
				code = new Code ()
						.setParentDir (template.getTargetDir ())
						.setFilename (engine.merge (template.getFilenameTemplate (), dataMap))
						.setContent (engine.merge (template.getContent (), dataMap));
			} else {
				code = new Code ()
						.setParentDir (template.getTargetDir ())
						.setFilename (engine.merge (template.getFilenameTemplate (), dataName, data))
						.setContent (engine.merge (template.getContent (), dataName, data));
			}

			return code;
		}
	}

	@Data
	@Accessors(chain = true)
	public static class Builder {
		private Engine engine = new VelocityTemplateEngine ();
		private DataSource<?> dataSource;
		private Object staticData;
		private TemplateSource templateSource;
		private Receiver receiver;
		private List<Interceptor> interceptorList = new ArrayList<> (4);
		private String dataName;
		private String staticDataName;

		public Builder addInterceptors(Interceptor... interceptors) {
			interceptorList.addAll (Arrays.asList (interceptors));
			return this;
		}

		public Generator build() {
			if (dataSource == null || templateSource == null || receiver == null) {
				throw new NullPointerException ("dataSource,templateSource and receiver must no null!");
			}

			if (StringUtils.isEmpty (staticDataName)) {
				staticDataName = staticData.getClass ().getSimpleName ().toLowerCase (Locale.ROOT);
			}
			log.info ("static data name: {}", staticDataName);

			if (StringUtils.isEmpty (dataName)) {
				dataName = dataSource.getDataName ();
			}
			log.info ("data name: {}", dataName);

			return new Generator (this);
		}
	}
}
