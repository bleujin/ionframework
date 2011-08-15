package net.ion.framework.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.ion.framework.db.rowset.CachedRowSet;
import net.ion.framework.db.rowset.WebRowSet;

public class RowsetExample
{

    Connection conn;

    public static void main( String[] args ) {
        try {
            String s1 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" ;
            StringBuffer s2 = new StringBuffer(8000) ;
            s2.append(s1) ;
            java.util.Date before = new java.util.Date() ;
            for(int i = 0 ; i< 10000 ; i++){
                s1 += "a" ;
            }
            System.out.println((new java.util.Date()).getTime() - before.getTime());

            before = new java.util.Date() ;
            for(int i = 0 ; i< 10000 ; i++){
                s2.append("a") ;
            }
            System.out.println((new java.util.Date()).getTime() - before.getTime());

        } catch ( Exception ex ) {
            System.out.println( ex );
        }
    }

    public String runSelectCachedRowset() throws SQLException, IOException, ClassNotFoundException {

        CachedRowSet cachedRs = null;
        cachedRs = new CachedRowSet();
        cachedRs.setCommand( "select * from clob1" );

        getConnection();
        cachedRs.execute( conn );
        freeConnection( conn );

        // Iterate through the result and print the employee names
        Writer writer = new StringWriter();

        while ( cachedRs.next() ) {
//			if (cachedRs.isBeforeFirst() || cachedRs.isLast() )
//				System.out.println("no rows ...");
//			cachedRs.absolute(++i) ;
            System.out.println( cachedRs.getType() );
            BufferedReader is = new BufferedReader( cachedRs.getClob( 1 ).getCharacterStream() );
            writer.write( is.readLine() );
            //System.out.println(cachedRs.getString(1));
        }
        writer.flush();
        writer.close();
        return writer.toString();
    }

    public String runSelectWebRowset() throws SQLException, IOException, ClassNotFoundException {

        WebRowSet webRs = null;
        webRs = new WebRowSet();
        // webRs.setCommand("select * from clob1") ;
        webRs.setCommand( "select * from emp where deptno = ?" );
        webRs.setInt( 1, 10 );

        getConnection();
        webRs.execute( conn );
        freeConnection( conn );

        // Writer writer = new FileWriter(new File("emp.xml")) ;
        Writer writer = new StringWriter();
        webRs.writeXml( writer );

        writer.flush();
        writer.close();

        return writer.toString();
    }

    public void runUpdateWebRowset() throws SQLException, IOException, ClassNotFoundException {
        WebRowSet webRs = null;
        webRs = new WebRowSet();
        // webRs.setCommand("select * from clob1") ;
        webRs.setCommand( "insert into AA values(?, ?)" );
        webRs.setInt( 1, 10 );
        webRs.setString( 2, "AAA" );

        getConnection();
        webRs.execute( conn );
        freeConnection( conn );

        System.out.println( webRs );
    }

    public void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName( "oracle.jdbc.driver.OracleDriver" );
        conn = DriverManager.getConnection(
               "jdbc:oracle:thin:@bleujin:1521:bleuora",
               "scott",
               "tiger" );
    }

    public void freeConnection( Connection conn ) throws SQLException {
        if ( conn != null )
            conn.close();
    }

//			Clob clob = null ;
//			cachedRs.setCommand("select empty_clob() from dual") ;
//			cachedRs.execute(conn) ;
//			cachedRs.next() ;
//			clob = cachedRs.getClob(1);
//
//			Writer writer = clob.setCharacterStream(4000) ;
//			String str = "衬快快快快旷" ;
//			writer.write(str) ;
//			writer.flush() ;
//			writer.close() ;

//		StringBuffer sb = new StringBuffer(10000) ;
//		String s = new String("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890") ;
//		for (int i = 0 ; i < 100 ; i++){
//			sb = sb.append(s) ;
//			i ++ ;
//		}
//		cachedRs.setCommand("insert into clob1 values(?)") ;
//		cachedRs.setObject(1, sb.toString()) ;
//		cachedRs.execute(conn) ;

}
