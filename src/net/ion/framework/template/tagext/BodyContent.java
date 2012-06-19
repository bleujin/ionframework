package net.ion.framework.template.tagext;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

/**
 * <p>
 * An encapsulation of the evaluation of the body of an action so it is available to a tag handler. BodyContent is a subclass of PageWriter.
 * </p>
 * <p>
 * Note that the content of BodyContent is the result of evaluation, so it will not contain actions and the like, but the result of their invocation.
 * </p>
 * <p>
 * BodyContent has methods to convert its contents into a String, to read its contents, and to clear the contents.
 * </p>
 * <p>
 * A BodyContent is made available to a BodyTag through a setBodyContent() call. The tag handler can use the object until after the call to doEndTag().
 * </p>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class BodyContent extends PageWriter {
	private PageWriter enclosingWriter = null;

	public BodyContent(PageWriter e) {
		super();
		enclosingWriter = e;
	}

	/**
	 * Clear the body without throwing any exceptions.
	 */
	public void clearBody() {
		clear();
	}

	/**
	 * Return the value of this BodyContent as a Reader.
	 * 
	 * @return
	 */
	public Reader getReader() {
		return new StringReader(toString());
	}

	/**
	 * Return the value of the BodyContent as a String.
	 * 
	 * @return
	 */
	public String getString() {
		return toString();
	}

	/**
	 * Write the contents of this BodyContent into a Writer.
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void writeOut(Writer writer) throws IOException {
		writer.write(getString());
		writer.flush();
	}

	/**
	 * Write the contents of this BodyContent into a Writer.
	 * 
	 * @param writer
	 */
	public void writeOut(PageWriter writer) {
		writer.write(getString());
		writer.flush();
	}

	/**
	 * Get the enclosing PageWriter
	 * 
	 * @return
	 */
	public PageWriter getEnclosingWriter() {
		return enclosingWriter;
	}
}
