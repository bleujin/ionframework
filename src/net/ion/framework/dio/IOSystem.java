package net.ion.framework.dio;

import java.io.IOException;
import java.net.URI;

import net.ion.framework.configuration.Configuration;

/**
 * <p>Title: IOEntry.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: I-ON Communications</p>
 * <p>Date : 2011. 1. 25.</p>
 * @author novision
 * @version 1.0
 */
public abstract class IOSystem {

	public static IOSystem get(URI uri, Configuration conf) {
		String scheme = uri.getScheme() ;
		String authority = uri.getAuthority() ;
		
		if (scheme == null){
			return get(conf) ;
		}
		return new MyLocalFileSystem(uri, conf);
	}

	private static IOSystem get(Configuration conf) {
		return get(URI.create("file:///"), conf);
	}
	
	public abstract void setWorkingDirectory(Path newPath);
	public abstract Path getWorkingDirectory() ;
	public abstract Path getHomeDirectory() ;

	public abstract FSInputStream open(Path path) throws IOException ;
	public abstract FSDataOutputStream create(Path path) throws IOException ;
	
	public abstract URI getUri() ;
}
