package com.szw.codegen.core;

import com.szw.codegen.core.entity.Template;

import java.util.Iterator;

/**
 * 模板源，一个模板的容器。
 * <p>
 * 生成器会从模板源中依次取出模板与数据相结合。
 *
 * @author szw
 */
public interface TemplateSource extends Iterable<Template> {

	/**
	 * 模板的迭代器
	 *
	 * @return 模板的迭代器
	 */
	@Override
	Iterator<Template> iterator();
}
