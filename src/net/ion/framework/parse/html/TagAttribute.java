package net.ion.framework.parse.html;


import net.htmlparser.jericho.Attribute;

public class TagAttribute {

	private Attribute attr ;
	TagAttribute(Attribute attr) {
		this.attr = attr ;
	}
	
	public String getKey(){
		return attr.getKey() ;
	}
	
	public String getValue(){
		return attr.getValue() ;
	}
}