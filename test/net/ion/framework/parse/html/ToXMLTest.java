package net.ion.framework.parse.html;

import java.io.StringReader;
import java.util.Iterator;

import net.htmlparser.jericho.Segment;
import net.ion.framework.util.Debug;

import junit.framework.TestCase;

public class ToXMLTest extends TestCase {

	public void testXML() throws Exception {
		String s = "<html><p>abc<center>°¡³ª´Ù</center>def</p></html>";
		HTag p = HTag.createGeneral(new StringReader(s), "html");

		Debug.debug(p.toXML().toString(), p.toString(), p.getElement().getChildElements().size(), p.getElement().getEnd());
		Debug.debug(p.getElement().encloses(1), p.getElement().getEndTag());

		Iterator<Segment> iter = p.getElement().getNodeIterator();
		while (iter.hasNext()) {
			Segment seg = iter.next();
			Debug.debug(seg, seg.getChildElements());
		}
	}
}
