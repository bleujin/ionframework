package net.ion.framework.db.procedure;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;
import net.ion.framework.util.ObjectUtil;

public class SerializedQuery implements Serializable, Queryable {

	private final static String PARAM = "_param";
	private final static String TYPE = "_type";
	private final static String QUERY = "_query";
	private final static String CQUERY = "_cquery";

	private String procSQL;
	private int queryType;
	private SerialType serialType;
	private Page page ;


	private Map<String, Serializable> queryParam = MapUtil.newMap();
	private Queryable deserializedQuery = null ;
	

	public enum SerialType {
		ParamQuery, UserProcedures, CombinedUserProcedures
	}

	private SerializedQuery(Queryable query, SerialType serialType) {
		this.procSQL = query.getProcSQL();
		this.queryType = query.getQueryType() ;
		this.serialType = serialType;
		this.page  = query.getPage() ;
	}

	static SerializedQuery createParameterQuery(ParameterQueryable aboutQuery) {
		List<Serializable> types = ListUtil.newList();
		List<Serializable> params = ListUtil.newList();
		aboutQuery.getProcSQL() ; // alert : namedparam -> param
		for (Object type : aboutQuery.getTypes()) {
			types.add((Serializable) type);
		}
		for (Object param : aboutQuery.getParams()) {
			params.add((Serializable) param);
		}

		
		final SerializedQuery serialQuery = new SerializedQuery(aboutQuery, SerialType.ParamQuery);
		serialQuery.queryParam.put(TYPE, (Serializable) types);
		serialQuery.queryParam.put(PARAM, (Serializable) params);

		return serialQuery;
	}

	public static <T extends IQueryable> T deserial(T query, IDBController dc){
		if (query instanceof UserProcedures){
			return (T)createUserProcedures((UserProcedures)query).deserializable(dc) ;
		} else if (query instanceof CombinedUserProcedures){
			return (T)createCombinedUserProcedures((CombinedUserProcedures)query).deserializable(dc) ;
		} else {
			return (T)createParameterQuery((ParameterQueryable)query).deserializable(dc) ;
		}
		
	}
	
	static SerializedQuery createCombinedUserProcedures(CombinedUserProcedures cupts) {

		final SerializedQuery serialQuery = new SerializedQuery(cupts, SerialType.CombinedUserProcedures);
		serialQuery.queryParam.put(CQUERY, (Serializable) cupts.getQuerys());
		return serialQuery;
	}

	static SerializedQuery createUserProcedures(UserProcedures upts) {

		final SerializedQuery serialQuery = new SerializedQuery(upts, SerialType.UserProcedures);
		serialQuery.queryParam.put(QUERY, (Serializable) upts.getQuerys());
		return serialQuery;
	}
	
	
	public Queryable deserializable(IDBController remoteDC) {

		if (this.serialType == SerialType.ParamQuery) {
			IParameterQueryable result;
			if (this.queryType == QueryType.USER_COMMAND || this.queryType == QueryType.USER_PROCEDURE) {
				result = remoteDC.createParameterQuery(this.procSQL);
			} else { // 
				result = remoteDC.createBatchParameterQuery(this.procSQL);
			}

			result.setParamValues(getParams(), getTypes());
			result.setPage(getPage());

			this.deserializedQuery = result ;
			return result;
		} else if (this.serialType == SerialType.UserProcedures) {
			IUserProcedures result = remoteDC.createUserProcedures(this.procSQL);
			for (SerializedQuery query : getQuerys()) {
				result.add(query.deserializable(remoteDC)) ;
			}
			this.deserializedQuery = result ;
			return result;
		} else if (this.serialType == SerialType.CombinedUserProcedures) {
			ICombinedUserProcedures result = remoteDC.createCombinedUserProcedures(this.procSQL);

			for (Query query : getCombinedQuery()) {
				result.add( ((SerializedQuery)query.getQuery()).deserializable(remoteDC) , query.getName(), query.getQueryType());
			}
			this.deserializedQuery = result ;
			return result;
		}

		throw new IllegalArgumentException("unknown type : " + this);
	}

	private List<Query> getCombinedQuery() {
		return ObjectUtil.coalesce((List) queryParam.get(CQUERY), ListUtil.EMPTY);

	}
	
	public SerialType getSerialType(){
		return serialType ;
	}

	public SerializedQuery[] getQuerys() {
		return (SerializedQuery[]) (ObjectUtil.coalesce((List) queryParam.get(QUERY), ListUtil.EMPTY)).toArray(new SerializedQuery[0]);
	}

	public List<Serializable> getParams() {
		return  ObjectUtil.coalesce((List<Serializable>) queryParam.get(PARAM), ListUtil.EMPTY);
	}

	private List getTypes() {
		return ObjectUtil.coalesce((List) queryParam.get(TYPE), ListUtil.EMPTY);
	}

	public Page getPage() {
		return this.page;
	}
	

	public long getCurrentModifyCount() {
		return deserializedQuery.getCurrentModifyCount();
	}

	public IDBController getDBController() {
		return deserializedQuery.getDBController();
	}

	public Statement getStatement() throws SQLException {
		return deserializedQuery.getStatement();
	}

	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		return deserializedQuery.myHandlerQuery(conn, handler);
	}

	public Rows myQuery(Connection conn) throws SQLException {
		return deserializedQuery.myQuery(conn);
	}

	public int myUpdate(Connection conn) throws SQLException {
		return deserializedQuery.myUpdate(conn);
	}

	public void cancel() throws SQLException, InterruptedException {
		deserializedQuery.cancel() ;
	}

	public Object execHandlerQuery(ResultSetHandler handler) throws SQLException {
		return deserializedQuery.execHandlerQuery(handler);
	}

	public Rows execPageQuery() throws SQLException {
		return deserializedQuery.execPageQuery();
	}

	public Rows execQuery() throws SQLException {
		return deserializedQuery.execQuery();
	}

	public int execUpdate() throws SQLException {
		return deserializedQuery.execUpdate();
	}

	public String getDBType() {
		return deserializedQuery.getDBType();
	}

	public String getProcFullSQL() {
		return deserializedQuery.getProcFullSQL();
	}

	public String getProcSQL() {
		return this.procSQL;
	}

	public int getQueryType() {
		return deserializedQuery.getQueryType();
	}

	public IQueryable setPage(Page page) {
		deserializedQuery.setPage(page) ;
		return this ;
	}

	public Serializable getParam(int i) {
		return getParams().get(i);
	}

}
