package net.ion.framework.mte.renderer;

import java.util.Iterator;
import java.util.Locale;

import net.ion.framework.mte.Renderer;
import net.ion.framework.mte.TemplateContext;



@SuppressWarnings("unchecked")
public class DefaultIterableRenderer implements Renderer<Iterable> {

	public String render(Iterable iterable, Locale locale) {
		final String renderedResult;

		final Iterator<?> iterator = iterable.iterator();
		renderedResult = iterator.hasNext() ? iterator.next().toString() : "";
		return renderedResult;

	}

}
