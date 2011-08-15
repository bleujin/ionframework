package net.ion.framework.db.meta;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DBParam {

	private String name;
	private String type;
	private int position;

	private static List<String> STRING_TYPE = new ArrayList<String>();
	private static List<String> NUMBER_TYPE = new ArrayList<String>();
	static {
		STRING_TYPE.add("varchar2");
		STRING_TYPE.add("varchar");
		STRING_TYPE.add("char");
	}
	static {
		NUMBER_TYPE.add("number");
	}

	DBParam(String name, String type, int position) {
		this.name = name;
		this.type = type;
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public String getTypeAsString() {
		return type;
	}

	public int getPosition() {
		return position;
	}

	public int getType() {
		if ("varchar".equalsIgnoreCase(getTypeAsString()) || "varchar2".equalsIgnoreCase(getTypeAsString())) {
			return Types.VARCHAR;
		} else if ("char".equalsIgnoreCase(getTypeAsString())) {
			return Types.CHAR;
		} else if ("number".equalsIgnoreCase(getTypeAsString())) {
			return Types.INTEGER;
		} else if ("clob".equalsIgnoreCase(getTypeAsString())) {
			return Types.CLOB;
		} else if ("raw".equalsIgnoreCase(getTypeAsString())) {
			return Types.BLOB;
		}
		return Types.OTHER;
	}

}
