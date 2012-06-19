package net.ion.framework.db.constant;

import java.util.logging.Level;

public class DefaultLogger implements ILogger {

	DefaultLogger() {
		this(Level.WARNING);
	}

	private Level logLevel;

	DefaultLogger(Level logLevel) {
		this.logLevel = logLevel;
	}

	public boolean isLevelEnabled(int level) {
		return level >= logLevel.intValue();
	}

	public void log(int level, String message) {
		if (isLevelEnabled(level))
			System.out.println(message);
	}

	public void log(int level, String message, Throwable exception) {
		if (isLevelEnabled(level))
			System.out.println(message);
	}

	public void init(RuntimeService service) throws Exception {

	}

	public void info(String message) {
		log(Level.INFO.intValue(), message);
	}

	public void warning(String message) {
		log(Level.WARNING.intValue(), message);
	}

	public static void main(String[] args) {
		DefaultLogger log = new DefaultLogger();
		log.info("info");
		log.warning("warning");
		log.log(Level.INFO.intValue(), "info");
	}
}
