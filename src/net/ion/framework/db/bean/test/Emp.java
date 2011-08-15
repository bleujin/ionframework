package net.ion.framework.db.bean.test;

import net.ion.framework.db.bean.shaping.IsChild;
import net.ion.framework.xml.XmlDocument;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class Emp implements IsChild {
	private int empNo;
	private String eName;
	private int sal;

	public Emp() {
	}

	public Object findPrimaryKey() {
		return new Integer(empNo);
	}

	public int getEmpNo() {
		return empNo;
	}

	public String getEName() {
		return eName;
	}

	public int getSal() {
		return sal;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public void setEName(String eName) {
		this.eName = eName;
	}

	public void setSal(int sal) {
		this.sal = sal;
	}

	public String toString() {
		return "empNo : " + getEmpNo() + ", eName : " + getEName();
	}

	public XmlDocument toXml() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<emp>\n");
		buffer.append("\t<empNo>" + empNo + "</empNo>\n");
		buffer.append("\t<eName>" + eName + "</eName>\n");
		buffer.append("</emp>\n");

		return new XmlDocument(buffer);
	}
}
