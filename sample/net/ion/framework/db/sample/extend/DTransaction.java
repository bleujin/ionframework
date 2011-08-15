package net.ion.framework.db.sample.extend;

import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.MSSQLDBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.db.procedure.MSSQL2000TxTransaction;
import net.ion.framework.db.procedure.OracleTxTransaction;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.db.procedure.TxTransaction;
import net.ion.framework.db.sample.SampleTestBase;
import net.ion.framework.db.xa.XAUserProcedure;

public class DTransaction extends SampleTestBase {

	/*
	 * 하나의 Connection에 대한 여러개의 DML에 대한 트랜잭션은 그냥 UserProcedures에다 집어넣어서 던지면 되는 아주 쉬운 문제이다.
	 * 
	 * 여기서는 여러개의 Connection(아마도 이기종DB)에 대한 여러개의 DML 트랜잭션에 대한 분산트랜잭션에 대한 예제이다.
	 */

	/*
	 * 분산 트랜잭션 예제는 MySQL이 아니라 MSSQL과 Oracle를 사용한 예제이다. MySQL은 얼마전까지 XaTransaction을 지원하지 않았고 현재도 InnoDB storage engine에서만 지원하는 걸로 알고 있다. 또 분산트랜잭션을 써야할 정도로 중요한 프로젝트에서는 MySQL을 사용해본적이 없어서 굳이 만들 필요성도 없었기 때문이기도 하다. 기타 등등의 이유로 관련자료도 거의 없기 때문에 아래의 예제는 오라클과
	 * MSSQL에서의 분산트랜잭션을 사용하기 위해서 프레임워크 사용방법이다.
	 * 
	 * 
	 * MSSQL에서 XaTransaction을 사용하기 위해. Be sure that you have copied your sqljdbc.dll file from C:\Program Files\Microsoft SQL Server 2000 Driver for JDBC\SQLServer JTA\ to your SQL Server's "binn" directory (most likely C:\Program Files\Microsoft SQL
	 * Server\MSSQL\Binn). Then open your instjdbc.sql script from C:\Program Files\Microsoft SQL Server 2000 Driver for JDBC\SQLServerJTA\ within Query Analyzer and run the script. This will install the extended stored procedures into SQL Server so
	 * that it can reference the sqljdbc.dll file.
	 * 
	 * 이상의 과정을 통해 보듯이 이기종 DB간의 트랜잭션은 매우 번거롭고 사전작업도 필요로 하는 DB가 있고 또 이것도 버전별로 다르다. 따라서 각각의 상황에 맞게 유동적으로 대응해야 한다.
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
