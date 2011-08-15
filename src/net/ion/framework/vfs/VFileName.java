package net.ion.framework.vfs;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileType;

public class VFileName {

	private FileName name;
	private VFileName(FileName name) {
		this.name = name ;
	}

	public static VFileName create(FileName name) {
		return new VFileName(name);
	}
	
	public String getScheme(){
		return name.getScheme() ;
	}
	
	public FileType getType(){
		return name.getType() ;
	}
	
	public VFileName getParent(){
		return create(name.getParent()) ;
	}
	
	public String getPath(){
		return name.getPath() ;
	}
	
	public String getExtension(){
		return name.getExtension() ;
	}
	
	public int getDepth(){
		return name.getDepth() ;
	}
	
	public String getURI(){
		return name.getURI() ;
	}
	
	public String getBaseName(){
		return name.getBaseName() ;
	}
	
	public VFileName getRoot(){
		return create(name.getRoot()) ;
	}

	public boolean equals(Object that){
		if (! (that instanceof VFileName)) return false ;
		return name.equals(((VFileName)that).name) ;
	}
	
	public int hashCode(){
		return name.hashCode() ;
	}
	
	public FileName getRealName(){
		return name ;
	}
	
	public String toString(){
		return name.toString() ;
	}
}
