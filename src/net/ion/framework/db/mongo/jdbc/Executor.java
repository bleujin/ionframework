// Executor.java

/**
 * Copyright (C) 2008 10gen Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ion.framework.db.mongo.jdbc;

import java.io.StringReader;
import java.util.List;
import java.util.Map.Entry;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Executor {

	private final DB mdb;
	private final String sql;
	private final Statement stmt;

	private List params;
	private int pos;

	Executor(DB db, String sql) throws MongoSQLException {
		this.mdb = db;
		this.sql = sql;
		this.stmt = initParse(sql);

	}

	void setParams(List params) {
		this.pos = 1;
		this.params = params;
	}

	DBCursor query() throws MongoSQLException {
		if (!(stmt instanceof Select))
			throw new IllegalArgumentException("not a query sql statement");

		Select select = (Select) stmt;
		if (!(select.getSelectBody() instanceof PlainSelect))
			throw new UnsupportedOperationException("can only handle PlainSelect so far");

		PlainSelect ps = (PlainSelect) select.getSelectBody();
		if (!(ps.getFromItem() instanceof Table))
			throw new UnsupportedOperationException("can only handle regular tables");

		DBCollection coll = getCollection((Table) ps.getFromItem());

		BasicDBObject fields = new BasicDBObject();
		for (Object o : ps.getSelectItems()) {
			SelectItem si = (SelectItem) o;
			if (si instanceof AllColumns) {
				if (fields.size() > 0)
					throw new UnsupportedOperationException("can't have * and fields");
				break;
			} else if (si instanceof SelectExpressionItem) {
				SelectExpressionItem sei = (SelectExpressionItem) si;
				fields.put(toFieldName(sei.getExpression()), 1);
			} else {
				throw new UnsupportedOperationException("unknown select item: " + si.getClass());
			}
		}

		// where
		DBObject query = parseWhere(ps.getWhere());

		// done with basics, build DBCursor
		DBCursor cur = coll.find(query, fields);

		{ // order by
			List orderBylist = ps.getOrderByElements();
			if (orderBylist != null && orderBylist.size() > 0) {
				BasicDBObject order = new BasicDBObject();
				for (int i = 0; i < orderBylist.size(); i++) {
					OrderByElement o = (OrderByElement) orderBylist.get(i);
					order.put(o.getColumnReference().toString(), o.isAsc() ? 1 : -1);
				}
				cur.sort(order);
			}
		}

		return cur;
	}

	int writeop() throws MongoSQLException {

		if (stmt instanceof Insert)
			return insert((Insert) stmt);
		else if (stmt instanceof Update)
			return update((Update) stmt);
		else if (stmt instanceof Drop)
			return drop((Drop) stmt);

		throw new RuntimeException("unknown write: " + stmt.getClass());
	}

	private int insert(Insert in) throws MongoSQLException {

		if (in.getColumns() == null)
			throw new MongoSQLException.BadSQL("have to give column names to insert");

		DBCollection coll = getCollection(in.getTable());

		if (!(in.getItemsList() instanceof ExpressionList))
			throw new UnsupportedOperationException("need ExpressionList");

		BasicDBObject o = new BasicDBObject();

		List valueList = ((ExpressionList) in.getItemsList()).getExpressions();
		if (in.getColumns().size() != valueList.size())
			throw new MongoSQLException.BadSQL("number of values and columns have to match");

		for (int i = 0; i < valueList.size(); i++) {
			o.put(in.getColumns().get(i).toString(), toConstant((Expression) valueList.get(i)));

		}

		coll.insert(o);
		return 1; // TODO - this is wrong
	}

	private int update(Update up) throws MongoSQLException {

		DBObject query = parseWhere(up.getWhere());

		BasicDBObject set = new BasicDBObject();

		for (int i = 0; i < up.getColumns().size(); i++) {
			String k = up.getColumns().get(i).toString();
			Expression v = (Expression) (up.getExpressions().get(i));
			set.put(k.toString(), toConstant(v));
		}

		DBObject mod = new BasicDBObject("$set", set);

		DBCollection coll = getCollection(up.getTable());
		coll.update(query, mod);
		return 1; // TODO
	}

	private int drop(Drop d) {
		DBCollection c = mdb.getCollection(d.getName());
		c.drop();
		return 1;
	}

	// ---- helpers -----

	String toFieldName(Expression e) {
		if (e instanceof StringValue)
			return e.toString().toUpperCase();
		if (e instanceof Column)
			return e.toString().toUpperCase();
		throw new UnsupportedOperationException("can't turn [" + e + "] " + e.getClass() + " into field name");
	}

	Object toConstant(Expression e) {
		if (e instanceof StringValue)
			return ((StringValue) e).getValue();
		else if (e instanceof DoubleValue)
			return ((DoubleValue) e).getValue();
		else if (e instanceof LongValue)
			return ((LongValue) e).getValue();
		else if (e instanceof NullValue)
			return null;
		else if (e instanceof JdbcParameter)
			return params.get(pos++);

		throw new UnsupportedOperationException("can't turn [" + e + "] " + e.getClass().getName() + " into constant ");
	}

	BasicDBObject parseWhere(Expression e) {
		BasicDBObject result = new BasicDBObject();
		if (e == null)
			return result;

		if (e instanceof EqualsTo) {
			EqualsTo eq = (EqualsTo) e;
			result.put(toFieldName(eq.getLeftExpression()), toConstant(eq.getRightExpression()));
		} else if (e instanceof GreaterThanEquals) {
			GreaterThanEquals ge = (GreaterThanEquals)e;
			result.put(toFieldName(ge.getLeftExpression()), new BasicDBObject().append("$gte", toConstant(ge.getRightExpression())));
		} else if (e instanceof GreaterThan) {
			GreaterThan ge = (GreaterThan)e;
			result.put(toFieldName(ge.getLeftExpression()), new BasicDBObject().append("$gt", toConstant(ge.getRightExpression())));
		} else if (e instanceof MinorThan) {
			MinorThan ge = (MinorThan)e;
			result.put(toFieldName(ge.getLeftExpression()), new BasicDBObject().append("$lt", toConstant(ge.getRightExpression())));
		} else if (e instanceof MinorThanEquals) {
			MinorThanEquals ge = (MinorThanEquals)e;
			result.put(toFieldName(ge.getLeftExpression()), new BasicDBObject().append("$lte", toConstant(ge.getRightExpression())));
		} else if (e instanceof NotEqualsTo) {
			NotEqualsTo ge = (NotEqualsTo)e;
			result.put(toFieldName(ge.getLeftExpression()), new BasicDBObject().append("$not", toConstant(ge.getRightExpression())));
		} else if (e instanceof AndExpression) {
			AndExpression ge = (AndExpression)e;
			BasicDBObject left = parseWhere(ge.getLeftExpression()) ;
			for (Entry<String, Object> entry : left.entrySet()){
				result.append(entry.getKey(), entry.getValue()) ;
			}
			BasicDBObject right = parseWhere(ge.getRightExpression()) ;
			for (Entry<String, Object> entry : right.entrySet()){
				result.append(entry.getKey(), entry.getValue()) ;
			}
		} else if (e instanceof OrExpression) {
			OrExpression ge = (OrExpression)e;
			BasicDBObject left = parseWhere(ge.getLeftExpression()) ;
			BasicDBObject right = parseWhere(ge.getRightExpression()) ;
			result.append("$or", new BasicDBObject[]{left, right}) ;
		} else {
			throw new UnsupportedOperationException("can't handle: " + e.getClass() + " yet");
		}

		return result;
	}

	private Statement initParse(String s) throws MongoSQLException {
		s = s.trim();
		try {
			return (new CCJSqlParserManager()).parse(new StringReader(s));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MongoSQLException.BadSQL(s);
		}
	}

	// ----

	DBCollection getCollection(Table t) {
		return mdb.getCollection(t.toString());
	}
	
	DB getDB(){
		return mdb ;
	}
	
	Statement getStatement(){
		return stmt ;
	}

}
