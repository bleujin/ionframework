package net.ion.framework.db;

import java.sql.Types;
import java.util.Map;

import junit.framework.TestCase;
import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.RandomUtil;

public class TestVirtualRows extends TestCase {

	public void testname() throws Exception {

		final Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(5000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

		Thread t2 = new Thread(new Runnable() {
			public void run() {

				if (false == t.isInterrupted()) {

				}
				;
				t.interrupt();
			}
		});

		t2.start();
	}

	public void testCreate() throws Exception {
		FakeRows rows = new FakeRows();

		rows.addColumn("a", Types.VARCHAR);
		rows.addColumn("b", Types.INTEGER);

		Map<String, Object> row = new CaseInsensitiveHashMap<Object>();
		row.put("a", "bleujin");
		row.put("b", 1);

		rows.addRow(row);

		assertEquals(1, rows.getRowCount());
	}

	public void testNext() throws Exception {
		FakeRows rows = new FakeRows();

		rows.addColumn("a", Types.VARCHAR);
		rows.addColumn("b", Types.INTEGER);

		for (int i = 0; i < 10; i++) {
			Map<String, Object> row = new CaseInsensitiveHashMap<Object>();
			row.put("a", RandomUtil.nextRandomString(7));
			row.put("b", i);
			rows.addRow(row);
		}

		assertEquals(10, rows.getRowCount());

		int i = 0;
		while (rows.next()) {
			assertEquals(String.valueOf(i), rows.getString("b"));
			i++;
		}
	}

}
