package net.ion.framework.template.ref;

import net.ion.framework.template.tagext.TemplateContext;

/**
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class GenericTemplateContext extends GenericContext implements TemplateContext {
	public GenericTemplateContext() {
		this(null);
	}

	public GenericTemplateContext(Context parent) {
		super(parent);
	}
}
