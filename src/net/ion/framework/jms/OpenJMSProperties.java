package net.ion.framework.jms;

import java.util.Properties;

import javax.naming.Context;

public class OpenJMSProperties extends Properties {
	public OpenJMSProperties(String url) {
		this("org.exolab.jms.jndi.rmi.RmiJndiInitialContextFactory", url);
	}

	public OpenJMSProperties(String contextFactory, String url) {
		put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
		put(Context.PROVIDER_URL, url);
	}
}
