package net.ion.framework.xml;

import java.io.Writer;
import java.sql.SQLException;

import net.ion.framework.db.Rows;

/**
 * net.ion.framework.db.Rows�� XML�̳� XML Schema�� java.io.Writer�� ����Ѵ�.
 * 
 * @author not attributable
 * @version 1.0
 */
public class XmlGenerator {
	SimpleXMLWriter xmlWriter = new SimpleXMLWriter();
	SimpleXSDWriter xsdWriter = new SimpleXSDWriter();

	public XmlGenerator() {
	}

	/**
	 * rows�� XML�� �����Ѵ�.
	 * 
	 * @param writer
	 *            Writer
	 * @param rows
	 *            Rows
	 * @throws SQLException
	 */
	public void toXML(Writer writer, Rows rows) throws SQLException {
		xmlWriter.writeXML(rows, writer);
	}

	/**
	 * rows�� XML�� ����Ѵ�. clob ����Ÿ���� XML�� �����Ͽ����� ������ �� ���ڵ��� replace �Ѵ�
	 * 
	 * @param writer
	 *            Writer
	 * @param rows
	 *            Rows
	 * @throws SQLException
	 */
	public void toXMLbyReplace(Writer writer, Rows rows) throws SQLException {
		xmlWriter.writeXMLbyReplace(rows, writer);
	}

	/**
	 * rows�� XML Schema�� �����Ѵ�.
	 * 
	 * @param writer
	 *            Writer
	 * @param rows
	 *            Rows
	 * @throws SQLException
	 */
	public void toXSD(Writer writer, Rows rows) throws SQLException {
		xsdWriter.writeXML(rows, writer);
	}

	public static void main(String[] args) {

		// long start = System.currentTimeMillis();
		// for(int i = 0; i < 20000; i++) {
		// String a = "";
		// for(int j = 0; j < 5; j++) {
		// a += "1";
		// }
		// }
		// System.out.println("time1 : " + (System.currentTimeMillis() - start));
		//
		// StringBuffer str2 = new StringBuffer("");
		//
		// start = System.currentTimeMillis();
		// for(int i = 0; i < 100000; i++) {
		// str2.append("1");
		// }
		// System.out.println("time2 : " + (System.currentTimeMillis() - start));
	}
}
