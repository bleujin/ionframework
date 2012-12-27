package net.ion.framework.db.sample.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.TestBaseSample;
import net.ion.framework.util.Debug;

public class UseServerHandler extends TestBaseSample {

	String query = "select * from millon_tblc where rownum < 10000";

	public void testUseFirst() throws Exception {
		IUserCommand cmd = (IUserCommand) dc.createUserCommand(query);
		Long time = cmd.execHandlerQuery(new OutHandler());

		Debug.debug(time);
	}
}

class OutHandler implements ResultSetHandler<Long> {

	private static final long serialVersionUID = 1060278026736572863L;

	public Long handle(ResultSet rs) throws SQLException {
		long startTime = System.currentTimeMillis();
		int rowcount = 0;
		while (rs.next()) {
			if ((rowcount++ % 10000) == 0)
				System.out.print(".");
			// Debug.debug(rs.getString(1));
		}
		System.out.println();
		return (System.currentTimeMillis() - startTime);
	}
}
