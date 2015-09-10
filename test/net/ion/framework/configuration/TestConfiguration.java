package net.ion.framework.configuration;

import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestConfiguration extends TestCase {


	public void testHasChild() throws Exception {
		ConfigurationFactory defaultConfigFactory = ConfigurationFactory.getInstance("ics-config");
		defaultConfigFactory.build(getClass().getResourceAsStream("test-config.xml"));
		Configuration cfg = defaultConfigFactory.getConfiguration("ics");
		
		Debug.line(cfg.hasChild("debug"), cfg.getChild("debug").getAttribute("enable"));
		Debug.line(cfg.hasChild("bleujin"), cfg.getChild("debug").getAttribute("enable"));
	}
	
}
