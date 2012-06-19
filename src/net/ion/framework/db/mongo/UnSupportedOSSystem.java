package net.ion.framework.db.mongo;

public class UnSupportedOSSystem extends RuntimeException {
	
	public UnSupportedOSSystem(){
		super("supported os : aix, hpux, linux, sun, win, zos") ;
	}

}
