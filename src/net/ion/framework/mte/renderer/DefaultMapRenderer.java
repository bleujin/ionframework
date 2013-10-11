package net.ion.framework.mte.renderer;

import java.util.Locale;
import java.util.Map;

import net.ion.framework.mte.Renderer;



@SuppressWarnings("rawtypes")
public class DefaultMapRenderer implements Renderer<Map> {

	public String render(Map map, Locale locale) {
		final String renderedResult;

		if (map.size() == 0) {
			renderedResult = "";
		} else if (map.size() == 1) {
			renderedResult = map.values().iterator().next().toString();
		} else {
			renderedResult = map.toString();
		}
		return renderedResult;

	}

}
