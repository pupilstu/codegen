package com.szw.codegen.core.engine;

import com.szw.codegen.core.Engine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

/**
 * Velocity模板引擎
 *
 * @author SZW
 */
public class VelocityTemplateEngine implements Engine {
	private final VelocityEngine ve = new VelocityEngine ();
	private final VelocityContext globalCtx = new VelocityContext ();

	@Override
	public void addStaticData(String staticDataName, Object staticData) {
		globalCtx.put (staticDataName, staticData);
	}

	@Override
	public void addStaticData(Map<?, ?> map) {
		for (Map.Entry<?, ?> entry : map.entrySet ()) {
			globalCtx.put (entry.getKey ().toString (), entry.getValue ());
		}
	}

	@Override
	public String mergeStatic(String template) {
		return merge (template, null);
	}

	@Override
	public String merge(String template, String dataName, Object data) {
		StringWriter writer = new StringWriter ();

		if (Objects.isNull (data)) {
			ve.evaluate (globalCtx, writer, "mergeTemplate", template);
			return writer.toString ();
		}

		VelocityContext ctx = new VelocityContext (globalCtx);
		ctx.put (dataName, data);
		ve.evaluate (ctx, writer, "mergeTemplate", template);
		return writer.toString ();
	}

	@Override
	public String merge(String template, Map<?, ?> data) {
		StringWriter writer = new StringWriter ();

		if (Objects.isNull (data) || data.isEmpty ()) {
			ve.evaluate (globalCtx, writer, "mergeTemplate", template);
			return writer.toString ();
		}

		VelocityContext ctx = new VelocityContext (globalCtx);
		for (Map.Entry<?, ?> entry : data.entrySet ()) {
			ctx.put (entry.getKey ().toString (), entry.getValue ());
		}
		ve.evaluate (ctx, writer, "mergeTemplate", template);
		return writer.toString ();
	}
}
