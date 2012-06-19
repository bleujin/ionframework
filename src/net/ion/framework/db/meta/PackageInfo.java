package net.ion.framework.db.meta;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.procedure.QueryType;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;

public class PackageInfo {

	private Map<String, DBFunction> functions = new CaseInsensitiveHashMap<DBFunction>();

	private PackageInfo() {
	}

	public static PackageInfo createOracle(Rows rows) throws SQLException {
		// make imsi map
		HashMap<String, DBFunction> imsi = new CaseInsensitiveHashMap<DBFunction>();
		while (rows.next()) {
			DBFunction fn = createOrGet(imsi, rows);
			DBParam param = new DBParam(rows.getString("argument_name"), rows.getString("data_type"), rows.getInt("position"));
			fn.addParam(param);

		}

		// make Real ;
		PackageInfo pinfo = new PackageInfo();
		HashMap<String, DBFunction> real = new CaseInsensitiveHashMap<DBFunction>();
		for (DBFunction dbf : imsi.values()) {
			real.put(StringUtil.deleteWhitespace(dbf.getProcString()), dbf);
		}
		pinfo.functions = real;

		return pinfo;
	}

	private static DBFunction createOrGet(Map<String, DBFunction> store, Rows rows) throws SQLException {
		String fnName = rows.getString("package_name") + "@" + rows.getString("object_name");
		if (store.containsKey(fnName)) {
			return store.get(fnName);
		} else {
			DBFunction dbf = new DBFunction(fnName);
			store.put(fnName, dbf);
			return dbf;
		}
	}

	public List<DBFunction> getFunction(String packageName) {
		List<DBFunction> result = new ArrayList<DBFunction>();

		for (String name : functions.keySet()) {
			if (name.startsWith(packageName.toLowerCase() + "@"))
				result.add(functions.get(name));
		}

		return result;
	}

	public boolean existFunction(String fnName) {
		return findFunction(fnName) != null;
	}

	public DBFunction findFunction(String fnName) {
		return functions.get(fnName);
	}

	public boolean fullCheck(IQueryable query) {
		if (query.getQueryType() == QueryType.USER_PROCEDURES) {
			IUserProcedures upts = (IUserProcedures) query;
			for (int i = 0; i < upts.size(); i++) {
				Queryable q = upts.getQuery(i);
				fullCheck(q);
			}
		} else {
			String fnName = StringUtil.deleteWhitespace(query.getProcSQL());
			boolean existFunction = existFunction(fnName);
			if (!existFunction) {
				Debug.line('=', "NOT FOUND", query.getProcSQL(), query.getQueryType());
				return false;
			}

			if (query instanceof IParameterQueryable) {
				IParameterQueryable pquery = (IParameterQueryable) query;
				List<Integer> types = pquery.getTypes();

				DBFunction dbf = findFunction(fnName);
				for (int i = 0; i < types.size(); i++) {
					int pType = types.get(i);
					int dbType = dbf.getParam(i + 1).getType();
					if (!isCompatableType(pType, dbType)) {
						Debug.line('$', "NOT MATCHED TYPE", query.getProcSQL(), types, dbf.getParamListType());
						continue;
					}
				}
			}
		}

		return true;
	}

	private boolean isCompatableType(int pType, int dbType) {
		return pType == dbType || (pType == Types.VARCHAR && dbType == Types.CHAR);
	}

	public DBPackage getPackage(String packageName) {
		return DBPackage.create(packageName, getFunction(packageName));
	}

}
