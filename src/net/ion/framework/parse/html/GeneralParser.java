package net.ion.framework.parse.html;

import java.io.IOException;
import java.io.Reader;

public class GeneralParser {

	private static GeneralParser self = new GeneralParser();

	private GeneralParser() {
	}

	// @TODO : ..
	// public final static HTag parse(Reader content) throws IOException {
	// // return parse(content, 0) ;
	// return parseGeneral(content) ;
	// }
	//
	// public final static HTag parse(Reader content, int index) throws IOException {
	// return self.parseContent(content, index) ;
	// }

	public final static HTag parseHTML(Reader reader) throws IOException {
		return HTag.createGeneral(reader, "html");
	}

	public final static HTag parseXML(Reader reader) throws IOException {
		return HTag.createGeneral(reader, "xml");
	}

	public final static HTag parseGeneral(Reader reader, String rootName) throws IOException {
		return HTag.createGeneral(reader, rootName);
	}

	private HTag parseContent(Reader reader, int index) throws IOException {
		return HTag.create(reader, index);
	}

}
