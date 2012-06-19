package net.ion.framework.db.mysql;

import java.io.IOException;
import java.io.Reader;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import net.ion.framework.db.RowsImpl;
import net.ion.framework.db.RowsUtils;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;

import org.apache.commons.io.IOUtils;

public class MySQLRowsImpl extends RowsImpl {

	private static final long serialVersionUID = -4236345721428284492L;

	public MySQLRowsImpl(Queryable query) throws SQLException {
		super(query);
	}

	public String getString(int i) throws SQLException {
		ResultSetMetaData meta = this.getMetaData();
		if (meta.getColumnType(i) == Types.CLOB) {
			if (this.getClob(i) == null)
				return StringUtil.EMPTY;
			else {
				return this.getClobString(i);
			}
			// } else if (meta.getColumnType(i) == -1 && meta.getColumnTypeName(i).equals("VARCHAR")){
			// return this.getClobString(i) ;
		} else {
			return super.getString(i);
		}
	}

	protected String getClobString(int i) throws SQLException {
		ResultSetMetaData meta = this.getMetaData();

		try {
			if (this.getObject(i) == null)
				return StringUtil.EMPTY;
			if (meta.getColumnType(i) == -1) {
				Reader reader = super.getCharacterStream(i);
				String result = IOUtils.toString(reader);
				reader.close();

				// StringWriter writer = new StringWriter();
				// IOUtils.
				// int bcount = IOUtils.copy(reader, writer);
				// reader.close() ;
				// writer.flush() ;
				Debug.line(result, result.length());
				return result;
			} else {
				return RowsUtils.clobToString(this.getClob(i));
			}
		} catch (IOException ex) {
			throw new SQLException(ex.getLocalizedMessage());
		}
		// else return ClobToString2(
		// ((OracleResultSet)this.getOriginalRow()).getCLOB(i) ) ;
	}

}
