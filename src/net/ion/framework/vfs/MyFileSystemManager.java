package net.ion.framework.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.ion.framework.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VfsLog;
import org.apache.commons.vfs2.impl.DefaultFileReplicator;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.PrivilegedFileReplicator;
import org.apache.commons.vfs2.operations.FileOperationProvider;
import org.apache.commons.vfs2.provider.FileProvider;
import org.apache.commons.vfs2.util.Messages;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyFileSystemManager extends DefaultFileSystemManager {
	private static final String CONFIG_RESOURCE = "providers.xml";
	private static final String PLUGIN_CONFIG_RESOURCE = "META-INF/vfs-providers.xml";

	private Log log = LogFactory.getLog(MyFileSystemManager.class);

	private URL configUri ;
	private ClassLoader classLoader;

	MyFileSystemManager(URL url) throws FileSystemException {
		setConfiguration(url) ;
	}

	/**
	 * Sets the configuration file for this manager.
	 * 
	 * @param configUri
	 *            The URI forthis manager.
	 */
	public void setConfiguration(final String configUri) {
		try {
			setConfiguration(new URL(configUri));
		} catch (MalformedURLException e) {
			log.warn(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Sets the configuration file for this manager.
	 * 
	 * @param configUri
	 *            The URI forthis manager.
	 */
	public void setConfiguration(final URL configUri) {
		this.configUri = configUri;
	}

	/**
	 * Sets the ClassLoader to use to load the providers. Default is to use the ClassLoader that loaded this class.
	 * 
	 * @param classLoader
	 *            The ClassLoader.
	 */
	public void setClassLoader(final ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Initializes this manager. Adds the providers and replicator.
	 * 
	 * @throws FileSystemException
	 *             if an error occurs.
	 */
	public void init() throws FileSystemException {
		// Set the replicator and temporary file store (use the same component)
		final DefaultFileReplicator replicator = createDefaultFileReplicator();
		setReplicator(new PrivilegedFileReplicator(replicator));
		setTemporaryFileStore(replicator);

		if (configUri == null) {
			// Use default config
			final URL url = getClass().getResource(CONFIG_RESOURCE);
			if (url == null) {
				throw new FileSystemException("vfs.impl/find-config-file.error", CONFIG_RESOURCE);
			}
			configUri = url;
		}
		configure(configUri); // Configure
		configurePlugins(); // Configure Plugins
		super.init(); // Initialise super-class
	}

	/**
	 * Scans the classpath to find any droped plugin.<br />
	 * The plugin-description has to be in /META-INF/vfs-providers.xml
	 * 
	 * @throws FileSystemException
	 *             if an error occurs.
	 */
	protected void configurePlugins() throws FileSystemException {
		ClassLoader cl = findClassLoader();

		Enumeration enumResources;
		try {
			enumResources = cl.getResources(PLUGIN_CONFIG_RESOURCE);
		} catch (IOException e) {
			throw new FileSystemException(e);
		}

		while (enumResources.hasMoreElements()) {
			URL url = (URL) enumResources.nextElement();
			configure(url);
		}
	}

	private ClassLoader findClassLoader() {
		if (classLoader != null) {
			return classLoader;
		}

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) {
			cl = getClass().getClassLoader();
		}

		return cl;
	}

	protected DefaultFileReplicator createDefaultFileReplicator() {
		return new DefaultFileReplicator();
	}

	/**
	 * Configures this manager from an XML configuration file.
	 * 
	 * @param configUri
	 *            The URI of the configuration.
	 * @throws FileSystemException
	 *             if an error occus.
	 */
	private void configure(final URL configUri) throws FileSystemException {
		InputStream configStream = null;
		try {
			// Load up the config
			// TODO - validate
			final DocumentBuilder builder = createDocumentBuilder();
			configStream = configUri.openStream();
			final Element config = builder.parse(configStream).getDocumentElement();

			configure(config);
		} catch (final Exception e) {
			throw new FileSystemException("vfs.impl/load-config.error", configUri.toString(), e);
		} finally {
			if (configStream != null) {
				try {
					configStream.close();
				} catch (IOException e) {
					log.warn(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	/**
	 * Configures this manager from an XML configuration file.
	 * 
	 * @param configUri
	 *            The URI of the configuration.
	 * @param configStream
	 *            An InputStream containing the configuration.
	 * @throws FileSystemException
	 *             if an error occurs.
	 */
	private void configure(final String configUri, final InputStream configStream) throws FileSystemException {
		try {
			// Load up the config
			// TODO - validate
			final DocumentBuilder builder = createDocumentBuilder();
			final Element config = builder.parse(configStream).getDocumentElement();

			configure(config);

		} catch (final Exception e) {
			throw new FileSystemException("vfs.impl/load-config.error", configUri, e);
		}
	}

	/**
	 * Configure and create a DocumentBuilder
	 * 
	 * @return A DocumentBuilder for the configuration.
	 * @throws ParserConfigurationException
	 *             if an error occurs.
	 */
	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		factory.setExpandEntityReferences(true);
		return factory.newDocumentBuilder();
	}

	/**
	 * Configures this manager from an parsed XML configuration file
	 * 
	 * @param config
	 *            The configuration Element.
	 * @throws FileSystemException
	 *             if an error occurs.
	 */
	private void configure(final Element config) throws FileSystemException {
		// Add the providers
		final NodeList providers = config.getElementsByTagName("provider");
		final int count = providers.getLength();
		for (int i = 0; i < count; i++) {
			final Element provider = (Element) providers.item(i);
			addProvider(provider, false);
		}

		// Add the operation providers
		final NodeList operationProviders = config.getElementsByTagName("operationProvider");
		for (int i = 0; i < operationProviders.getLength(); i++) {
			final Element operationProvider = (Element) operationProviders.item(i);
			addOperationProvider(operationProvider);
		}

		// Add the default provider
		final NodeList defProviders = config.getElementsByTagName("default-provider");
		if (defProviders.getLength() > 0) {
			final Element provider = (Element) defProviders.item(0);
			addProvider(provider, true);
		}

		// Add the mime-type maps
		final NodeList mimeTypes = config.getElementsByTagName("mime-type-map");
		for (int i = 0; i < mimeTypes.getLength(); i++) {
			final Element map = (Element) mimeTypes.item(i);
			addMimeTypeMap(map);
		}

		// Add the extension maps
		final NodeList extensions = config.getElementsByTagName("extension-map");
		for (int i = 0; i < extensions.getLength(); i++) {
			final Element map = (Element) extensions.item(i);
			addExtensionMap(map);
		}
	}

	/**
	 * Adds an extension map.
	 * 
	 * @param map
	 *            containing the Elements.
	 */
	private void addExtensionMap(final Element map) {
		final String extension = map.getAttribute("extension");
		final String scheme = map.getAttribute("scheme");
		if (scheme != null && scheme.length() > 0) {
			addExtensionMap(extension, scheme);
		}
	}

	/**
	 * Adds a mime-type map.
	 * 
	 * @param map
	 *            containing the Elements.
	 */
	private void addMimeTypeMap(final Element map) {
		final String mimeType = map.getAttribute("mime-type");
		final String scheme = map.getAttribute("scheme");
		addMimeTypeMap(mimeType, scheme);
	}

	/**
	 * Adds a provider from a provider definition.
	 * 
	 * @param providerDef
	 *            the provider definition
	 * @param isDefault
	 *            true if the default should be used.
	 * @throws FileSystemException
	 *             if an error occurs.
	 */
	private void addProvider(final Element providerDef, final boolean isDefault) throws FileSystemException {
		final String classname = providerDef.getAttribute("class-name");

		// Make sure all required schemes are available
		final String[] requiredSchemes = getRequiredSchemes(providerDef);
		for (int i = 0; i < requiredSchemes.length; i++) {
			final String requiredScheme = requiredSchemes[i];
			if (!hasProvider(requiredScheme)) {
				final String msg = Messages.getString("vfs.impl/skipping-provider-scheme.debug", new String[] { classname, requiredScheme });
				VfsLog.debug(getLogger(), log, msg);
				return;
			}
		}

		// Make sure all required classes are in classpath
		final String[] requiredClasses = getRequiredClasses(providerDef);
		for (int i = 0; i < requiredClasses.length; i++) {
			final String requiredClass = requiredClasses[i];
			if (!findClass(requiredClass)) {
				final String msg = Messages.getString("vfs.impl/skipping-provider.debug", new String[] { classname, requiredClass });
				VfsLog.debug(getLogger(), log, msg);
				return;
			}
		}

		// Create and register the provider
		final FileProvider provider = (FileProvider) createInstance(providerDef);
		final String[] schemas = getSchemas(providerDef);
		if (schemas.length > 0) {
			addProvider(schemas, provider);
		}

		// Set as default, if required
		if (isDefault) {
			setDefaultProvider(provider);
		}
	}

	/**
	 * Adds a operationProvider from a operationProvider definition.
	 */
	private void addOperationProvider(final Element providerDef) throws FileSystemException {
		final String classname = providerDef.getAttribute("class-name");

		// Attach only to available schemas
		final String[] schemas = getSchemas(providerDef);
		for (int i = 0; i < schemas.length; i++) {
			final String schema = schemas[i];
			if (hasProvider(schema)) {
				final FileOperationProvider operationProvider = (FileOperationProvider) createInstance(providerDef);
				addOperationProvider(schema, operationProvider);
			}
		}
	}
	
	/**
	 * Tests if a class is available.
	 */
	private boolean findClass(final String className) {
		try {
			findClassLoader().loadClass(className);
			return true;
		} catch (final ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Extracts the required classes from a provider definition.
	 */
	private String[] getRequiredClasses(final Element providerDef) {
		final ArrayList<String> classes = new ArrayList<String>();
		final NodeList deps = providerDef.getElementsByTagName("if-available");
		final int count = deps.getLength();
		for (int i = 0; i < count; i++) {
			final Element dep = (Element) deps.item(i);
			String className = dep.getAttribute("class-name");
			if (className != null && className.length() > 0) {
				classes.add(className);
			}
		}
		return classes.toArray(new String[classes.size()]);
	}

	/**
	 * Extracts the required schemes from a provider definition.
	 */
	private String[] getRequiredSchemes(final Element providerDef) {
		final ArrayList<String> schemes = new ArrayList<String>();
		final NodeList deps = providerDef.getElementsByTagName("if-available");
		final int count = deps.getLength();
		for (int i = 0; i < count; i++) {
			final Element dep = (Element) deps.item(i);
			String scheme = dep.getAttribute("scheme");
			if (scheme != null && scheme.length() > 0) {
				schemes.add(scheme);
			}
		}
		return schemes.toArray(new String[schemes.size()]);
	}

	/**
	 * Extracts the schema names from a provider definition.
	 */
	private String[] getSchemas(final Element provider) {
		final ArrayList<String> schemas = new ArrayList<String>();
		final NodeList schemaElements = provider.getElementsByTagName("scheme");
		final int count = schemaElements.getLength();
		for (int i = 0; i < count; i++) {
			final Element scheme = (Element) schemaElements.item(i);
			schemas.add(scheme.getAttribute("name"));
		}
		return schemas.toArray(new String[schemas.size()]);
	}

	/**
	 * Creates a provider.
	 */
	private FileProvider createInstance(final Element ele) throws FileSystemException {
		try { 
			final Class clazz = findClassLoader().loadClass(ele.getAttribute("class-name"));
			
			FileProvider fp = (FileProvider)clazz.newInstance() ;
			NodeList children = ele.getChildNodes() ;
			for (int i = 0 ; i < children.getLength() ; i++) {
				final Node node = children.item(i);
				if ("property".equals(node.getNodeName())) {
					Element enode = (Element)node ;
					final Method method = fp.getClass().getMethod("set" + StringUtil.capitalize(enode.getAttribute("name")), String.class) ;
					method.invoke(fp, enode.getAttribute("value")) ;
				}
			}
			
			
			
			return fp;
		} catch (final Exception e) {
			throw new FileSystemException("vfs.impl/create-provider.error", ele, e);
		}
	}
}
