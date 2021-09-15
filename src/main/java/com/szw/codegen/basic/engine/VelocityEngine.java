package com.szw.codegen.basic.engine;

import org.apache.velocity.VelocityContext;

import java.io.StringWriter;

/**
 * @author SZW
 * @date 2021/8/22
 */
public class VelocityEngine implements Engine {

	private final org.apache.velocity.app.VelocityEngine ve = new org.apache.velocity.app.VelocityEngine ();

	private final VelocityContext globalCtx = new VelocityContext ();

	VelocityContext ctx = new VelocityContext ();


	@Override
	public void addConfig(String name, Object o) {
		globalCtx.put (name, o);
	}

	@Override
	public <T> String merge(String templateStr, T bean, String beanName) {
		ctx = new VelocityContext (globalCtx);
		ctx.put (beanName, bean);

		StringWriter writer = new StringWriter ();
		ve.evaluate (ctx, writer, "mergeTemplate", templateStr);

		return writer.toString ();
	}
}
