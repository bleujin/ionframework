package net.ion.framework.db.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ion.framework.util.StringUtil;

public class DBFunction {

	private String name;
	private Map<Integer, DBParam> params = new HashMap<Integer, DBParam>();

	public DBFunction(String name) {
		this.name = name;
	}

	public void addParam(DBParam param) {
		params.put(param.getPosition(), param);
	}

	public String toString() {
		return this.name;
	}
	
	public String getName(){
		return this.name ;
	}

	public boolean isProcedureType() {
		return params.get(0) == null;
	}

	public DBParam getParam(int i) {
		return params.get(i);
	}

	public List<Integer> getParamListType() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 1; i <= params.size(); i++) {
			if (getParam(i) != null)
				result.add(getParam(i).getType());
		}
		return result;
	}

	public int getParamSize() {
		return params.size();
	}

	public String getProcString() {
		List<Character> qmark = new ArrayList<Character>();
		for (DBParam param : params.values()) {
			if (param.getPosition() > 0)
				qmark.add('?');
		}
		return name + "(" + StringUtil.join(qmark.toArray(new Character[0]), ',') + ")";
	}
}
