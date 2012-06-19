package net.ion.framework.db.bean.handlers;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import net.ion.framework.db.DBTestCaseByBleujin;
import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.util.Debug;

public class TestPageRowsHandler extends DBTestCaseByBleujin {

	private IUserProcedure upt;

	public void setUp() throws Exception {
		super.setUp();
		upt = dc.createUserProcedure("Sample@selectEmpBy()");
		// upt.addParam(20) ;
	}

	public void testCall() throws Exception {
		Rows rows = upt.execQuery();
		rows.writeXml(new BufferedWriter(new OutputStreamWriter(System.out)));
	}

	public void testPage() throws Exception {
		final Page page2 = Page.create(10, 2);
		upt.setPage(page2);

		Debug.debug(page2, upt.getPage());
	}

	public void testHandler() throws Exception {
		final Page page2 = Page.create(7, 12, 7);
		upt.setPage(page2);

		Rows rows = upt.execPageQuery();

		Debug.line("CURRENT", rows.getRowCount(), rows.getScreenInfo());
		assertEquals(true, rows.getScreenInfo().hasNextScreen());

		final Rows nextPageRows = rows.nextPageRows();
		Debug.line("NEXT", nextPageRows.getRowCount(), rows.getScreenInfo());
		assertEquals(true, rows.getScreenInfo().hasPreScreen());

		while (nextPageRows.next()) {
			nextPageRows.getString(1);
		}
	}
}
