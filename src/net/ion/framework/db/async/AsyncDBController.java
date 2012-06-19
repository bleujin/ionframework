package net.ion.framework.db.async;

import java.util.Date;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IQueryable;

// Future Pattern.Host
public class AsyncDBController {
	private final IDBController dc;

	public AsyncDBController(IDBController dc) {
		this.dc = dc;
	}

	public Data getRows(final String query) {
		return getRows(dc.createUserCommand(query));
	}

	public Data getRows(final IQueryable query) {
		final FutureData future = new FutureData(query);
		new Thread() {
			public void run() {
				try {
					Rows rows = query.execQuery();
					RealData realdata = new RealData(query, rows);
					future.setRealData(realdata);
				} catch (Exception ex) {
					future.setException(ex);
				}
			}
		}.start();

		return future;
	}

	public Result execUpdate(final String query) {
		return execUpdate(dc.createUserCommand(query));
	}

	public Result execUpdate(final IQueryable query) {
		final Date startDate = new Date();
		final FutureResult future = new FutureResult(query, startDate);

		new Thread() {
			public void run() {
				try {
					int rowcount = query.execUpdate();
					RealResult realresult = new RealResult(query, startDate, rowcount);
					future.setRealResult(realresult);
				} catch (Exception ex) {
					future.setException(ex);
				}
			}
		}.start();

		return future;
	}
}
