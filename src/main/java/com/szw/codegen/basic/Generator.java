package com.szw.codegen.basic;

import com.szw.codegen.basic.engine.Engine;
import com.szw.codegen.basic.engine.VelocityEngine;
import com.szw.codegen.core.util.BeanPropUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Freemaker的官网手册上有这样一句话：模板 + 数据模型 = 输出。
 * <p>
 * 在实际场景中，例如java样板代码生成，通常是templates + datas = outputs，codegen所做的事，
 * 就是自动的从TemplateSource（模板源）和DataSource（数据源）中获取templates和datas，
 * 并将templates与datas合并得到outputs
 * <p>
 * data 包含两部分，config和bean，config为全局数据，作用域全局，可以在任何模板中使用它。
 * <p>
 * 程序会针对每个bean并根据每个模板生成一套代码
 *
 * @author SZW
 * @date 2021/8/15
 */
@Slf4j
public class Generator {

	private final Engine engine;

	private final String codeConfigName;

	private final String beanConfigName;

	private final String templateConfigName;

	private final String additionalConfigName;

	private String dataBeanName;


	public Generator(Engine engine, String codeConfigName, String beanConfigName, String templateConfigName, String additionalConfigName, String dataBeanName) {
		this.engine = engine;
		this.codeConfigName = codeConfigName;
		this.beanConfigName = beanConfigName;
		this.templateConfigName = templateConfigName;
		this.additionalConfigName = additionalConfigName;
		this.dataBeanName = dataBeanName;
	}

	public Generator(Engine engine, String dataBeanName) {
		this (engine, "cc", "bc", "tc", "ac", dataBeanName);
	}

	public Generator(String dataBeanName) {
		this (new VelocityEngine (), dataBeanName);
	}

	public <T> void generate(DataSource ds, TemplateSource<T> ts, Receiver<T> receiver, Class<T> classT, Map<String, Object> additionalConfig) {
		// Add configs.
		engine.addConfig (codeConfigName, receiver.getConfig ());
		engine.addConfig (beanConfigName, ds.getConfig ());
		engine.addConfig (templateConfigName, ts.getConfig ());
		engine.addConfig (additionalConfigName, additionalConfig);

		//NotNull check.
		Iterator<Object> dataItr = ds.iterator ();
		if (!dataItr.hasNext ()) {
			log.warn ("No Beans available, will generate nothing.");
			return;
		}
		Iterator<T> templateItr = ts.iterator ();
		if (!templateItr.hasNext ()) {
			log.warn ("No template available, will generate nothing.");
			return;
		}

		//Data bean name.
		Object firstData = dataItr.next ();
		if (dataBeanName == null || "".equals (dataBeanName)) {
			dataBeanName = firstData.getClass ().getSimpleName ().toLowerCase (Locale.ROOT);
		}
		log.info ("dataBeanName: {}", dataBeanName);

		try {
			//Properties
			List<BeanPropUtil.Property<T>> properties = BeanPropUtil.getProperties (classT, true);

			while (templateItr.hasNext ()) {
				T next = templateItr.next ();

				while (dataItr.hasNext ()) {
					Object data = dataItr.next ();

					T result = classT.getConstructor ().newInstance ();

					for (BeanPropUtil.Property<T> property : properties) {
						Object o = property.get (next);
						String val = engine.merge (o.toString (), data, dataBeanName);
						property.set (result, val);
					}

					receiver.receive (result);
				}
				// Required !!!
				dataItr = ds.iterator ();
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.error ("An error occurred in resolving bean {},please check this bean and your config,then restart.", classT.getName ());
			e.printStackTrace ();
		}
	}
}
