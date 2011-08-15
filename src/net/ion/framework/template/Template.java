package net.ion.framework.template;

/**
 * Inner template, Outer template으로 구성되며 inner template은 page tag를 가질 수 없다.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public interface Template {
	static final int TYPE_OUTER_TEMPLATE = 0;
	static final int TYPE_INNER_TEMPLATE = 1;

	String getTemplateText();

	int getTemplateType();
}
