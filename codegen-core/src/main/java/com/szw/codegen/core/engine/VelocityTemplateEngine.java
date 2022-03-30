package com.szw.codegen.core.engine;

import com.szw.codegen.core.Engine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

/**
 * Velocity模板引擎。
 *
 * @author SZW
 */
public class VelocityTemplateEngine implements Engine {
	public static final String LOG_TAG = "velocity";

	private final VelocityEngine ve = new VelocityEngine ();
	private final VelocityContext globalCtx = new VelocityContext ();
	private String dataName;

	@Override
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

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
	public String merge(String template, Object data) {

		if (template == null) {
			return null;
		}

		StringWriter writer = new StringWriter ();

		if (Objects.isNull (data)) {
			ve.evaluate (globalCtx, writer, LOG_TAG, template);
			return writer.toString ();
		}

		VelocityContext ctx = new VelocityContext (globalCtx);
		ctx.put (dataName, data);

		ve.evaluate (ctx, writer, LOG_TAG, template);
		return writer.toString ();
	}

	@Override
	public String merge(String template, Map<?, ?> data) {

		if (template == null) {
			return null;
		}

		StringWriter writer = new StringWriter ();

		if (Objects.isNull (data) || data.isEmpty ()) {
			ve.evaluate (globalCtx, writer, LOG_TAG, template);
			return writer.toString ();
		}

		VelocityContext ctx = new VelocityContext (globalCtx);
		for (Map.Entry<?, ?> entry : data.entrySet ()) {
			ctx.put (entry.getKey ().toString (), entry.getValue ());
		}

		ve.evaluate (ctx, writer, LOG_TAG, template);
		return writer.toString ();
	}
}
