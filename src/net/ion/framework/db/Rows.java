package net.ion.framework.db;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.RowSet;
import javax.sql.RowSetInternal;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IQueryable;

/**
 * CLOB precessing and Adapter Pattern
 * <p>
 * Title: Database framework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public interface Rows extends RowSet, RowSetInternal, Serializable, Cloneable {
	public static String DEFAULT_ROOT_NAME = "RowSet";
	public static String DEFAULT_ROW_NAME = "row";

	public static String PUBLIC_DTD_ID = "-//Sun Microsystems, Inc.//DTD RowSet//EN";
	public static String SYSTEM_ID = "http://java.sun.com/j2ee/dtds/RowSet.dtd";

	public int getRowCount();

	public void populate(ResultSet rs) throws SQLException;

	public void populate(ResultSet rs, int skip, int length) throws SQLException;

	public Rows setNextRows(Rows rows);

	public Rows getNextRows();

	public void setXmlWriter(IXmlWriter writer) throws SQLException;

	public void writeXml(Writer writer, IXmlWriter xmlWriter) throws SQLException;

	public void writeXml(Writer writer) throws SQLException, IOException;

	public IXmlWriter getIXmlWriter() throws SQLException;

	public boolean getShowDeleted() throws SQLException;

	public String getTableName() throws SQLException;

	public void setTableName(String name) throws SQLException;

	public String getDefaultString(String col1, String defaultString) throws SQLException;

	public Row firstRow();

	public Rows nextPageRows() throws SQLException;

	public Rows prePageRows() throws SQLException;

	public Rows refreshRows(boolean useCache) throws SQLException;

	public Collection toCollection() throws SQLException;

	public Object toHandle(ResultSetHandler rsh) throws SQLException;

	public IQueryable getQueryable();

	public void clearQuery();

	public ScreenInfo getScreenInfo();
	
	public Rows toClone() throws IOException ;
}

// MSSQL
// dc.setDBManager( new MSSQLDBManager( "com.microsoft.jdbc.sqlserver.SQLServerDriver",
// "jdbc:microsoft:sqlserver://bleujin:1433;DatabaseName=pubs",
// "scott",
// "tiger"
// ) );
//
// UserProcedure upt = new UserProcedure( "Insert_Test_Clob3 (?,?,?) " );
// upt.addParam( 0, 999 );
// upt.addParam( 1, "나나나나나" );
// upt.addBlob( 2, "c:\\odbcconf.log" );
// dc.execProcedureUpdate( upt );
//
// upt = new UserProcedure( "Select_test_clob4(?)" );
// upt.addParam( 0, 999 );
// Rows rows = dc.getRows( upt );
//
// while ( rows.MoveNext() )
// {
// Vector currRow = rows.getCurrentRow();
// System.out.print( currRow.get( 0 ) + ", " );
// System.out.print( currRow.get( 1 ) + ", " );
// System.out.print( currRow.get( 2 ) + ", " );
// System.out.println( "" );
// }
//
// dc.execUpdate( "delete from clobtest where a = 999" );

/**
 * Iterator Pattern(Java Enumeration) haxNext, next, remove
 * 
 * @return
 */
// private Vector convertCollection(){
// Vector arr = null ;
// try
// {
// arr = new Vector();
// if(next())
// {
// arr.add( getCurrentTuple() );
// }
// return arr;
// }
// catch(SQLException ex)
// {
// return arr;
// }
// }
//
// public Iterator iterator()
// {
// return convertCollection().iterator() ;
// }
// public int size(){
// return convertCollection().size() ;
// }
// public boolean isEmpty()
// {
// return convertCollection().isEmpty() ;
// }
// public boolean contains(Object o)
// {
// return convertCollection().contains(o) ;
// }
// public Object[] toArray()
// {
// return convertCollection().toArray() ;
// }
// public Object[] toArray(Object[] a)
// {
// return convertCollection().toArray(a) ;
// }
// public boolean add(Object o)
// {
// return convertCollection().add(o);
// }
// public boolean remove(Object o)
// {
// return convertCollection().remove(o) ;
// }
// public boolean containsAll(Collection c)
// {
// return convertCollection().containsAll(c) ;
// }
// public boolean addAll(Collection c)
// {
// return convertCollection().addAll(c) ;
// }
// public boolean removeAll(Collection c)
// {
// return convertCollection().removeAll(c) ;
// }
// public boolean retainAll(Collection c)
// {
// return convertCollection().retainAll(c) ;
// }
// public void clear()
// {
// convertCollection().clear() ;
// }
// public boolean equals(Object o)

/**
 * Adapter Pattern(MS식 Iterator) MoveNext, MovePrevious, MoveLast, MoveFirst, MoveBeforeFirst, MoveAfterLast, MoveAt
 * 
 * @return
 */
// public boolean MoveNext() throws SQLException {
// return next();
// }
//
// public boolean MovePrevious() throws SQLException {
// return previous();
// }
//
// public void Reset() throws SQLException {
// beforeFirst();
// }
//
// public void MoveLast() throws SQLException {
// last();
// }
//
// public void MoveFirst() throws SQLException {
// first();
// }
//
// public void MoveBeforeFirst() throws SQLException {
// beforeFirst();
// }
//
// public void MoveAfterLast() throws SQLException {
// afterLast();
// }
//
// public boolean MoveAt(int movepoint) throws SQLException {
// return absolute(movepoint);
// }

