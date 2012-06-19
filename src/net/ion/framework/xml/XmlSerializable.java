package net.ion.framework.xml;

import java.io.Serializable;

import net.ion.framework.xml.excetion.XmlException;

/**
 * Data를 XML로 바꾸어 저장할 때 필요한 Interface
 * 
 * @author bleujin
 * @version 1.0
 */

public interface XmlSerializable extends Serializable {
	XmlDocument toXml() throws XmlException;
}
