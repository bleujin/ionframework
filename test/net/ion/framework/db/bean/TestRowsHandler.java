package net.ion.framework.db.bean;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.ecs.xhtml.b;

import net.ion.framework.db.DBTestCase;
import net.ion.framework.db.Rows;
import net.ion.framework.util.Debug;

public class TestRowsHandler extends DBTestCase {

	public void testFormBean() throws Exception {
		Rows rows = dc.createUserCommand("select to_number('1') depth, 2 interval, 'T' bvalue from dual").execQuery();
		RowsHandler handler = new RowsHandler();
		FormBean formBean = new FormBean();
		handler.saveFormBean(rows, formBean);

		assertEquals(1, formBean.getDepth());
		assertEquals(2, formBean.getInterval());
		assertEquals(true, formBean.isBvalue());
		
		Debug.debug(rows) ;
		
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream output = new ObjectOutputStream(bout) ;
		output.writeObject(rows) ;
		
		Rows readRows =  (Rows) new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray())).readObject() ;
		readRows.beforeFirst() ;
		while(readRows.next()){
			Debug.debug(readRows.getString(1), bout.toByteArray().length, readRows) ;
		}
	}

}

class FormBean {
	private int depth;
	private int interval;
	private boolean bvalue;

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getDepth() {
		return this.depth;
	}

	public int getInterval() {
		return this.interval;
	}

	public void setBvalue(boolean bvalue) {
		this.bvalue = bvalue;
	}

	public boolean isBvalue() {
		return this.bvalue;
	}
}
