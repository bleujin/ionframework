package net.ion.framework.mte.renderer;

import java.util.List;
import java.util.Locale;

import net.ion.framework.mte.Renderer;
import net.ion.framework.mte.TemplateContext;
import net.ion.framework.mte.util.Util;


public class DefaultObjectRenderer implements Renderer<Object> {

	public String render(Object value, Locale locale) {
		final String renderedResult;

		if (value instanceof String) {
			renderedResult = (String) value;
		} else {
			final List<Object> arrayAsList = Util.arrayAsList(value);
			if (arrayAsList != null) {
				renderedResult = arrayAsList.size() > 0 ? arrayAsList.get(0)
						.toString() : "";
			} else {
				renderedResult = value.toString();
			}
		}
		return renderedResult;
	}
}
