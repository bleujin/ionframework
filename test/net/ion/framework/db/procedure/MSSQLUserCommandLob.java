package net.ion.framework.db.procedure;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;

import net.ion.framework.db.DBTestCase;
import net.ion.framework.db.Rows;
import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;
import net.ion.framework.util.RandomUtil;

public class MSSQLUserCommandLob extends DBTestCase {

	public void testFirst() throws Exception {

		Rows rows = dc.createUserCommand("select 1").execQuery();
		Debug.debug(rows);
	}

	public void testLobString() throws Exception {
		deleteA();

		IUserCommand ins = dc.createUserCommand("insert into lob_sample(a, c) values(:a, :c)");
		ins.addParam("a", 1);
		ins.addClob("c", makeLongString(20000));
		ins.execUpdate();

		selectA();

	}

	public void testLobReader() throws Exception {
		deleteA();

		IUserCommand ins = dc.createUserCommand("insert into lob_sample(a, c) values(:a, :c)");
		ins.addParam("a", 1);
		ins.addClob("c", new StringReader(makeLongString(20000)));
		ins.execUpdate();

		selectA();

	}

	public void testBatch() throws Exception {
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into performance_sample values(:a, :b, :c)");
		int max = 10000;
		for (int i = 0; i < max; i++) {
			cmd.addBatchParam("a", i);
			cmd.addBatchParam("b", String.valueOf(i));
			cmd.addBatchParam("c", String.valueOf(i));
		}
		cmd.execUpdate();
	}

	public void testProcedureBatch() throws Exception {
		IUserProcedureBatch cmd = dc.createUserProcedureBatch("sample@insertWith(?, ?)");
		int max = 10000;
		for (int i = 0; i < max; i++) {
			cmd.addBatchParam(0, i);
			cmd.addBatchParam(1, String.valueOf(i));
		}
		cmd.execUpdate();
	}

	public void testLobBatch() throws Exception {
		int max = 5000;
		Reader reader = createTempFile();
		IUserProcedures procs = dc.createUserProcedures("insert lob");
		for (int i = 0; i < max; i++) {
			IUserCommand cmd = dc.createUserCommand("insert into lob_sample(a, b, c) values(?, ?, ?)");
			cmd.addParam(0, i);
			cmd.addParam(1, String.valueOf(i));
			cmd.addClob(2, new StringReader(makeLongString(5000)));
			procs.add(cmd);
		}
		procs.execUpdate();
	}

	private Reader createTempFile() throws IOException {
		File file = IOUtil.createTempFile("framework");
		FileWriter writer = new FileWriter(file);
		writer.write(makeLongString(10000));
		writer.close();

		return new FileReader(file);
	}

	private void selectA() throws SQLException {
		IUserCommand sel = dc.createUserCommand("select * from lob_sample where a= :a");
		sel.addParam("a", 1);
		Debug.debug(sel.execQuery());
	}

	private void deleteA() throws SQLException {
		IUserCommand del = dc.createUserCommand("delete from lob_sample where a = :a");
		del.addParam("a", 1);
		del.execUpdate();
	}

	private String makeLongString(int length) {
		return RandomUtil.nextRandomString(length);
	}
}
