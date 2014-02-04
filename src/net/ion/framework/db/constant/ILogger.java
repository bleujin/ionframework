package net.ion.framework.db.constant;

public interface ILogger {

	public void init(RuntimeService service) throws Exception;

	public boolean isLevelEnabled(int level);

	public void log(int level, String message);

	public void log(int level, String message, Throwable exception);

	public void info(String string);

	public void warning(String string);
}
