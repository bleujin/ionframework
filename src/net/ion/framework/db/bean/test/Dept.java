package net.ion.framework.db.bean.test;

import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.bean.shaping.IsChild;
import net.ion.framework.db.bean.shaping.IsParent;
import net.ion.framework.xml.XmlDocument;
import net.ion.framework.xml.XmlSerializable;
import net.ion.framework.xml.excetion.XmlException;

public class Dept implements IsParent {

	private String dName;
	private String loc;

	private List<IsChild> child = new ArrayList<IsChild>();
	private int deptNo;

	public Dept() {
	}

	public int getDeptNo() {
		return deptNo;
	}

	public String getDName() {
		return dName;
	}

	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}

	public void setDName(String DName) {
		this.dName = DName;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getLoc() {
		return loc;
	}

	public Object findPrimaryKey() {
		return new Integer(deptNo);
	}

	public void addChild(IsChild obj) {
		child.add(obj);
	}

	public IsChild getChild(int index) {
		return (IsChild) child.get(index);
	}

	public IsChild[] getChilds() {

		return child.toArray(new IsChild[0]);
	}

	public void removeChild(IsChild obj) {
		child.remove(obj.findPrimaryKey());
	}

	public int getChildLength() {
		return child.size();
	}

	public String toString() {
		return "deptNo : " + getDeptNo() + ", dName : " + getDName() + ", empCount : " + getChildLength();
	}

	public XmlDocument toXml() throws XmlException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<dept deptNo=\"" + deptNo + "\">\n");
		buffer.append("<dName>" + dName + "</dName>\n");
		for (int i = 0; i < child.size(); i++) {
			buffer.append(((XmlSerializable) child.get(i)).toXml().getXmlString());
		}
		buffer.append("</dept>\n");
		return new XmlDocument(buffer);

	}

}
