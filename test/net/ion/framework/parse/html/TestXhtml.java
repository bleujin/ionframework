package net.ion.framework.parse.html;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;
import junit.framework.TestCase;

public class TestXhtml extends TestCase {

	
	public void testToXHTML() throws Exception {
		HTag tag = HTag.createGeneral(new StringReader("<html><body><p>a1<img />b1 <div>c1</div> a2</p></body></html>"), "html") ;
		
		tag.visit(new TagVisitor() {
			public void visit(HTag tag) throws NotFoundTagException, IOException {
				Debug.println(StringUtil.repeat("\t", tag.getDepth()), tag.getTagName(), tag.getChildren().size(), tag.getElement().getBegin(), tag.getElement().getEnd(), tag.getElement().getContent()) ;
				
				Debug.debug(tag.getElement().getEndTag().generateHTML(tag.getTagName())) ;
			}
		}) ;
		
		
	}
}
