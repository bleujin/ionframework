package net.ion.framework.db.sample.rows;

import net.ion.framework.db.Row;
import net.ion.framework.db.Rows;
import net.ion.framework.db.sample.SampleTestBase;

public class RowsFirstRow extends SampleTestBase {

	String query = "select * from copy_sample";

	public void testFirstRow() throws Exception {
		Rows rows = dc.getRows(query);

		Row row = rows.firstRow();

		assertEquals("01", row.getString(2));
		assertEquals("1", row.getString(1));

		assertEquals("01", row.getString("no2"));
		assertEquals("1", row.getString("no1"));

		assertEquals("1", row.getString("no1")); // auto convert int -> string
	}
}
