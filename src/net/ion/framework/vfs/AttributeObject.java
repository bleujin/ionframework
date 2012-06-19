package net.ion.framework.vfs;

import org.apache.commons.vfs2.FileName;

public class AttributeObject {

	private FileName name ;
	private Object value ;
	private AttributeObject(FileName name, Object value){
		this.name = name ;
		this.value = value ;
	}
	
	public static AttributeObject create(FileName name, Object value) {
		return new AttributeObject(name, value);
	}

	
	public FileName getFileName(){
		return name ;
	}
	
	public Object getValue(){
		return value ;
	}
	
}
