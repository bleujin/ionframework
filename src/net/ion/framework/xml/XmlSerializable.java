package net.ion.framework.xml;

import java.io.Serializable;

import net.ion.framework.xml.excetion.XmlException;

/**
 * Data�� XML�� �ٲپ� ������ �� �ʿ��� Interface
 * 
 * @author bleujin
 * @version 1.0
 */

public interface XmlSerializable extends Serializable {
	XmlDocument toXml() throws XmlException;
}
