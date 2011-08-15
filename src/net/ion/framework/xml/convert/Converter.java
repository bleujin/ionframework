package net.ion.framework.xml.convert;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.ion.framework.xml.excetion.XmlException;

import org.xml.sax.SAXException;

public class Converter {

	public static void toOffice(Reader xmlReader, Reader xslReader, OutputStream output) throws XmlException {
		toOffice(xmlReader, xslReader, output, null);
	}

	public static void toOffice(Reader xmlReader, Reader xslReader, OutputStream output, String charSet) throws XmlException {
		try {
			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xslReader));

			// Start XSLT transformation
			if (charSet == null) {
				transformer.transform(new StreamSource(xmlReader), new StreamResult(output));
			} else {
				StringWriter writer = new StringWriter();
				transformer.transform(new StreamSource(xmlReader), new StreamResult(writer));

				String result = writer.toString();

				BufferedOutputStream bos = new BufferedOutputStream(output);
				OutputStreamWriter osw = new OutputStreamWriter(bos, "ISO-8859-1");
				osw.write(new String(result.getBytes(charSet), "ISO-8859-1"));
				osw.flush();
				osw.close();
				bos.close();
			}
		} catch (Exception e) {
			throw new XmlException(e);
		} finally {
			try {
				output.close();
			} catch (IOException ex) {
			}
		}
	}

	public static void toPDF(Reader xmlReader, Reader xslReader, OutputStream output, File fopConfig) throws UnsupportedEncodingException,
			TransformerConfigurationException, TransformerException, IOException, SAXException {

		FactorySingleton single = FactorySingleton.getInstance(fopConfig, output);

		TransformerFactory factory = single.getTransformerFactory();
		Transformer transformer = factory.newTransformer(new StreamSource(xslReader));
		transformer.setParameter("versionParam", "2.0");
		// Resulting SAX events (the generated FO) must be piped through to FOP

		Result res = new SAXResult(single.getFop().getDefaultHandler());
		// Start XSLT transformation and FOP processing
		try {
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859");
			transformer.transform(new StreamSource(xmlReader), res);
			output.flush();
		} catch (TransformerException ex) {
			throw ex;
		} finally {
			try {
				output.close();
			} catch (IOException ex) {
			}
		}

		// make Custom Font
		// java -cp
		// lib\fop.jar;lib\avalon-framework-4.2.0.jar;lib\xercesImpl-2.7.1.jar
		// org.apache.fop.fonts.apps.TTFReader font\JA_BODY.TTF JA_BODY.xml
		// java -cp
		// lib\fop.jar;lib\avalon-framework-4.2.0.jar;lib\xercesImpl-2.7.1.jar
		// org.apache.fop.fonts.apps.TTFReader font\KO_BODY.TTF KO_BODY.xml
		// java -cp
		// lib\fop.jar;lib\avalon-framework-4.2.0.jar;lib\xercesImpl-2.7.1.jar
		// org.apache.fop.fonts.apps.TTFReader font\KO_TITLE1.TTF KO_TITLE1.xml
		// java -cp
		// lib\fop.jar;lib\avalon-framework-4.2.0.jar;lib\xercesImpl-2.7.1.jar
		// org.apache.fop.fonts.apps.TTFReader font\KO_TITLE2.TTF KO_TITLE2.xml
	}

}
