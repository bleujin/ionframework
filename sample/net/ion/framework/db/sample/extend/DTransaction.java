package net.ion.framework.db.sample.extend;

import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.MSSQLDBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.db.procedure.MSSQL2000TxTransaction;
import net.ion.framework.db.procedure.OracleTxTransaction;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.db.procedure.TxTransaction;
import net.ion.framework.db.sample.TestBaseDB;
import net.ion.framework.db.xa.XAUserProcedure;

public class DTransaction extends TestBaseDB {

	/*
	 * �ϳ��� Connection�� ���� �������� DML�� ���� Ʈ������� �׳� UserProcedures���� ���־ ������ �Ǵ� ���� ���� �����̴�.
	 * 
	 * ���⼭�� �������� Connection(�Ƹ��� �̱���DB)�� ���� �������� DML Ʈ����ǿ� ���� �л�Ʈ����ǿ� ���� �����̴�.
	 */

	/*
	 * �л� Ʈ����� ������ MySQL�� �ƴ϶� MSSQL�� Oracle�� ����� �����̴�. MySQL�� ������� XaTransaction�� �������� �ʾҰ� ���絵 InnoDB storage engine������ �����ϴ� �ɷ� �˰� �ִ�. �� �л�Ʈ������� ����� ������ �߿��� ������Ʈ������ MySQL�� ����غ����� �� ���� ���� �ʿ伺�� ���� �����̱⵵ �ϴ�. ��Ÿ ����� ������ ����ڷᵵ ���� ��� ������ �Ʒ��� ������ ����Ŭ��
	 * MSSQL������ �л�Ʈ������� ����ϱ� ���ؼ� �����ӿ�ũ ������̴�.
	 * 
	 * 
	 * MSSQL���� XaTransaction�� ����ϱ� ����. Be sure that you have copied your sqljdbc.dll file from C:\Program Files\Microsoft SQL Server 2000 Driver for JDBC\SQLServer JTA\ to your SQL Server's "binn" directory (most likely C:\Program Files\Microsoft SQL
	 * Server\MSSQL\Binn). Then open your instjdbc.sql script from C:\Program Files\Microsoft SQL Server 2000 Driver for JDBC\SQLServerJTA\ within Query Analyzer and run the script. This will install the extended stored procedures into SQL Server so
	 * that it can reference the sqljdbc.dll file.
	 * 
	 * �̻��� ������ ���� ������ �̱��� DB���� Ʈ������� �ſ� ��ŷӰ� �����۾��� �ʿ�� �ϴ� DB�� �ְ� �� �̰͵� ����� �ٸ���. ��� ������ ��Ȳ�� �°� ���������� �����ؾ� �Ѵ�.
	 */

	public void testSimple() throws Exception {

		// prepare
		MSSQLDBManager mManager = new MSSQLDBManager("jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=test", "bleu", "redf");
		DBController mdc = new DBController(mManager);
		mdc.initSelf();

		OracleDBManager oManager = new OracleDBManager("jdbc:oracle:thin:@novision:1521:al", "al", "redf");
		DBController odc = new DBController(oManager);
		odc.initSelf();

		// tx1 : mssql
		TxTransaction tx1 = new MSSQL2000TxTransaction(mManager, "mquery");
		tx1.add((Queryable) mdc.createUserCommand("insert into copy_tblc values('111', 111)"));

		// tx2 : oracle
		TxTransaction tx2 = new OracleTxTransaction(oManager, "oquery");
		tx2.add((Queryable) odc.createUserCommand("insert into copy_tblc values('111', 111)"));

		// add tx
		TxTransaction[] txs = new TxTransaction[] { tx1, tx2 };
		XAUserProcedure upts = new XAUserProcedure(txs);
		upts.execUpdate();

		// clear
		mdc.destroySelf();
		odc.destroySelf();
	}
}
