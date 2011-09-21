package net.ion.framework.db;

import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.servant.AfterTask;
import net.ion.framework.db.servant.IExtraServant;
import net.ion.framework.db.servant.NoneServant;

/**
 * ��� Database Query ������ Front Facade ������ �ϴ� Ŭ���� Used Facade Pattern
 * .execUpdate, .execQuery(Dynamic Query) <- ����ȿ���� ���� �����Ƿ� ������ ������� ����
 * .execCommandUpdate, .execCommandQuery(Parameter Query) .execProcedureUpdate,
 * .execProcedureQuery(Procedure Query & Lob Type Query) ���� : ����Ŭ�� Manager�� �������
 * ��� limitedRow�� �� ����� �ȵ�(ref cursor�� �̿��Ͽ� ���� row�� �����ϴ� ����Ŭ Function ������ ��������
 * ����..)
 */

public class BatchDBController extends DBController { // implements
														// Configurable

	private IExtraServant servant = null;

	public BatchDBController(String name, DBManager dbm) {
		this(name, dbm, new NoneServant());
	}

	public BatchDBController(String name, DBManager dataBaseManager, IExtraServant servant) {
		super(name, dataBaseManager);
		this.servant = servant;
	}

    protected void handleServant(long start, long end, IUserProcedures query, int execType) {
        servant.support(new AfterTask(start, end, this.getDBManager(), query, execType));
    }

    public void handleServant(long start, long end, IQueryable query, int execType) {
        servant.support(new AfterTask(start, end, this.getDBManager(), query, execType));
    }

}
