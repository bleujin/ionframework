package net.ion.framework.mte.renderer;

import java.util.Collection;
import java.util.Locale;

import net.ion.framework.mte.Renderer;


@SuppressWarnings("rawtypes")
public class DefaultCollectionRenderer implements Renderer<Collection> {

	public String render(Collection collection, Locale locale) {
		final String renderedResult;

		if (collection.size() == 0) {
			renderedResult = "";
		} else if (collection.size() == 1) {
			renderedResult = collection.iterator().next().toString();
		} else {
			renderedResult = collection.toString();
		}
		return renderedResult;

	}

}
