package net.ion.framework.db.sample;

import net.ion.framework.db.sample.dc.ConnectionLess;
import net.ion.framework.db.sample.dc.DCInit;
import net.ion.framework.db.sample.dc.RowsToXML;
import net.ion.framework.db.sample.extend.CombinedQuery;
import net.ion.framework.db.sample.extend.LobHandle;
import net.ion.framework.db.sample.extend.TestNamedParameter;
import net.ion.framework.db.sample.extend.QueryPage;
import net.ion.framework.db.sample.first.P1_SelectQuery;
import net.ion.framework.db.sample.first.P2_UpdateQuery;
import net.ion.framework.db.sample.first.P3_BatchCommandQuery;
import net.ion.framework.db.sample.first.P4_UserProcedure;
import net.ion.framework.db.sample.first.P5_UserProceduresQuery;
import net.ion.framework.db.sample.first.P6_UserProceduresUpdate;
import net.ion.framework.db.sample.handler.UseHandler;
import net.ion.framework.db.sample.plan.ViewPlan;
import net.ion.framework.db.sample.rows.RowsFirstRow;
import net.ion.framework.db.sample.rows.RowsGet;
import net.ion.framework.db.sample.rows.RowsPage;
import net.ion.framework.db.sample.servant.TestExtraServant;
import net.ion.framework.db.servant.TestChannelServant;
import net.ion.framework.util.Debug;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class DBSampleAllTest {
	public static Test suite() {
		System.setProperty(Debug.PROPERTY_KEY, "off");

		TestSuite suite = new TestSuite(DBSampleAllTest.class.getPackage().getName());

		// dc
		suite.addTestSuite(DCInit.class);
		suite.addTestSuite(ConnectionLess.class);

		suite.addTestSuite(TestExtraServant.class) ;
		suite.addTestSuite(TestChannelServant.class) ;
		
		// first
		suite.addTestSuite(P1_SelectQuery.class);
		suite.addTestSuite(P2_UpdateQuery.class);
		suite.addTestSuite(P3_BatchCommandQuery.class);
		suite.addTestSuite(P4_UserProcedure.class);
		suite.addTestSuite(P5_UserProceduresQuery.class);
		suite.addTestSuite(P6_UserProceduresUpdate.class);

		// extend
		suite.addTestSuite(CombinedQuery.class);
		// suite.addTestSuite(DTransaction.class) ; // MSSQL�� Xa Configuration�� DTC�� ����� �����Ǿ� �־�� �Ѵ�.
		suite.addTestSuite(LobHandle.class);
		suite.addTestSuite(TestNamedParameter.class);
		suite.addTestSuite(QueryPage.class);

		// handler
		suite.addTestSuite(UseHandler.class);

		// plan
		suite.addTestSuite(ViewPlan.class);

		// rows
		suite.addTestSuite(RowsFirstRow.class);
		suite.addTestSuite(RowsGet.class);
		suite.addTestSuite(RowsPage.class);
		suite.addTestSuite(RowsToXML.class) ;

		return suite;
	}

	public static void main(String[] args) {
		TestRunner.run(DBSampleAllTest.suite());
	}
}
