package net.ion.framework.parse.html;

import java.io.IOException;

public interface TagVisitor {
	public void visit(HTag tag) throws IOException;
}
