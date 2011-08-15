package net.ion.framework.xml.convert;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerFactory;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

public class FactorySingleton {

	private static FactorySingleton self = null;

	private TransformerFactory factory = null;
	private Fop fop = null;

	private FactorySingleton(File fopConfig, OutputStream outputStream) throws SAXException, IOException {
		init(fopConfig, outputStream);
	}

	public static synchronized FactorySingleton getInstance(File fopConfig, OutputStream outputStream) throws SAXException, IOException {
		// if (self == null) {
		self = new FactorySingleton(fopConfig, outputStream);
		// }
		return self;
	}

	private void init(File fopConfig, OutputStream outputStream) throws SAXException, IOException {
		// make new fopFactory instance
		FopFactory fopFactory = FopFactory.newInstance();
		fopFactory.setUserConfig(fopConfig);
		fopFactory.setBaseURL(fopConfig.getParent());

		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputStream);

		// Setup logger
		// Logger logger = new ConsoleLogger( ConsoleLogger.LEVEL_INFO );
		// driver.setLogger( logger );
		// driver.reset();

		// Setup Renderer (output format)
		// driver.setRenderer( Driver.RENDER_PDF );
		// driver.setOutputStream( outputStream );

		// Setup XSLT
		factory = TransformerFactory.newInstance();
	}

	public TransformerFactory getTransformerFactory() {
		return factory;
	}

	public Fop getFop() {
		return fop;
	}

}
