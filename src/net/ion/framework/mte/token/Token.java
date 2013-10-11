package net.ion.framework.mte.token;

import net.ion.framework.mte.Engine;
import net.ion.framework.mte.TemplateContext;


/**
 * Internal structure returned by the {@link Lexer} passing parsed information
 * into the {@link Engine}.
 */
public interface Token extends Cloneable {

	/**
	 * Returns the text of the token.
	 * 
	 * @return the text
	 */
	public String getText();

	public int getLine();

	public int getColumn();

	public String getSourceName();
	
	public Object evaluate(TemplateContext context);
	
	public int getTokenIndex();
	
	public String emit();
}
