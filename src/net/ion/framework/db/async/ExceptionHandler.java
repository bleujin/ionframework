package net.ion.framework.db.async;


public interface ExceptionHandler {
	public final static ExceptionHandler PrintHandler = new ExceptionHandler() {
		public void handle(Throwable ex) {
			ex.printStackTrace() ;
		}
	}; 
	
	public void handle(Throwable ex) ;
}
