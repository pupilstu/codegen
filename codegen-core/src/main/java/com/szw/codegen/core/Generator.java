package com.szw.codegen.core;

import com.szw.codegen.core.engine.VelocityTemplateEngine;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author SZW
 */
@Slf4j
public class Generator {

	public static <T, R> Executor<T, R> executor() {
		return new Executor<> ();
	}

	/**
	 * @param <T> 模板类型
	 * @param <R> 结果类型
	 */
	@Data
	@Accessors(chain = true)
	public static class Executor<T, R> {
		private Engine engine = new VelocityTemplateEngine ();

		private Iterable<?> datas;
		private Object staticData;


		private Iterable<T> templates;

		private Receiver<R> receiver;

		private MergeStrategy<T, R> mergeStrategy;

		private String dataName = "data";
		private String staticDataName = "static";

		private final List<Interceptor<T, R>> interceptorList = new ArrayList<> (4);
		private final InterceptorAddIt interceptorAddIt = new InterceptorAddIt ();

		public InterceptorAddIt addInterceptors() {
			return interceptorAddIt;
		}

		private void notNullCheck(Object o, String name) {
			if (o == null) {
				throw new NullPointerException ("The property '" + name + "' must not be null!");
			}
		}

		/**
		 * 属性设置之后，{@link #execute()} 之前执行，对属性设置进行检查。
		 */
		private void afterPropertiesSet() {
			// 非空检查
			notNullCheck (datas, "datas");
			notNullCheck (templates, "templates");
			notNullCheck (receiver, "receiver");
			notNullCheck (mergeStrategy, "mergeStrategy");

			// Map 数据不需要使用 staticDataName
			if (StringUtils.isEmpty (staticDataName) && !(staticData instanceof Map)) {
				staticDataName = staticData.getClass ().getSimpleName ().toLowerCase (Locale.ROOT);
				log.info ("static data name: {}", staticDataName);
			}
			if (StringUtils.isEmpty (dataName)) {
				dataName = getDataName (datas);
				log.info ("data name: {}", dataName);
			}

			/* ==================== 初始化相关 ==================== */

			// 向拦截链结尾添加 MergeInterceptor
			interceptorList.add (new MergeInterceptor ());

			// 设置 dataName
			engine.setDataName (dataName);

			// 添加静态数据
			if (staticData instanceof Map) {
				engine.addStaticData ((Map<?, ?>) staticData);
			} else {
				engine.addStaticData (staticDataName, staticData);
			}

		}

		public void execute() {

			// 属性设置检查
			afterPropertiesSet ();

			Iterator<?> dataItr = datas.iterator ();
			if (!dataItr.hasNext ()) {
				log.warn ("None data is available, will generate nothing.");
				return;
			}

			Iterator<T> templateItr = templates.iterator ();
			if (!templateItr.hasNext ()) {
				log.warn ("None template is available, will generate nothing.");
				return;
			}

			// 遍历模板，执行合并
			while (templateItr.hasNext ()) {
				T template = templateItr.next ();

				R result = mergeStrategy.doMerge (template, staticData, engine);
				if (result != null) {
					receiver.accept (result);
					continue;
				}

				while (dataItr.hasNext ()) {
					Object data = dataItr.next ();

					Iterator<Interceptor<T, R>> interceptorItr = interceptorList.iterator ();

					result = new ChainImpl<> (interceptorItr)
							.proceed (template, data);

					receiver.accept (result);
				}

				// 重置遍历器
				dataItr = datas.iterator ();
			}
		}

		private String getDataName(Iterable<?> data) {
			Type[] genericInterfaces = data.getClass ().getGenericInterfaces ();
			for (Type genericInterface : genericInterfaces) {
				if (genericInterface instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
					if (parameterizedType.getRawType ().equals (Iterable.class)) {
						return ((Class<?>) parameterizedType.getActualTypeArguments ()[0])
								.getSimpleName ()
								.toLowerCase (Locale.ROOT);
					}
				}
			}
			return dataName;
		}

		/**
		 * 拦截链上的最后一个拦截器，执行模板和数据合并的任务
		 */
		private class MergeInterceptor implements Interceptor<T, R> {
			@Override
			public R intercept(T template, Object data, Chain<T, R> chain) {
				if (data == null) {
					return mergeStrategy.doMerge (template, staticData, engine);
				}

				return mergeStrategy.doMerge (template, data, staticData, engine);
			}
		}

		public class InterceptorAddIt {
			public InterceptorAddIt addNext(Interceptor<T, R> interceptor) {
				interceptorList.add (interceptor);
				return InterceptorAddIt.this;
			}

			public Executor<T, R> finished() {
				return Executor.this;
			}
		}
	}
}
