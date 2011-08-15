package net.ion.framework.db.mongo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import net.ion.framework.rope.Rope;
import net.ion.framework.rope.RopeBuilder;
import net.ion.framework.rope.RopeReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

public class MongoParamUtils {

	 public static final int COMMAND_LOC = 1 ;
	 
	public static void setParam(Connection conn, PreparedStatement pstmt, List _param, List _types, int mediateLoc) throws IOException, SQLException{
        Object[] params = (Object[])_param.toArray(new Object[0]) ;
        int[] types = ArrayUtils.toPrimitive((Integer[])_types.toArray(new Integer[0]) ) ;

        for(int i = 0, last = params.length; i < last; i++) {
            Object obj = params[i];
            int type = types[i];
            if(isNull(obj)) {
                pstmt.setNull(i + mediateLoc, types[i]);
            } else if(isClobType(type)) {
            	if (obj instanceof CharSequence) {
            		pstmt.setObject(i + mediateLoc, obj.toString());
            	} else {
            		Reader reader = (Reader)obj ;
            		String str = IOUtils.toString(reader) ;
            		pstmt.setString(i + mediateLoc, str) ;
            		reader.close() ;
            	}
            } else if(isBlobType(type)) {
                throw new UnsupportedOperationException("blob not supported yet") ;
            } else {
                pstmt.setObject(i + mediateLoc, obj);
            }
        }
	}
	

    private static boolean isNull(Object obj){
        return obj == null;
    }
    private static boolean isClobType(int type){
        return Types.CLOB == type;
    }
    private static boolean isBlobType(int type){
        return Types.BLOB == type ;
    }


    public static void setCommandParam(PreparedStatement pstmt, List params, List types) throws SQLException {
    	setCommandParam(pstmt, params, types, COMMAND_LOC) ;
    }
	public static void setCommandParam(PreparedStatement pstmt, List _param, List _types, int mediateLoc) throws SQLException {
		Object[] params = (Object[]) _param.toArray(new Object[0]);
		int[] types = ArrayUtils.toPrimitive((Integer[]) _types.toArray(new Integer[0]));

		for (int i = 0, last = params.length; i < last; i++) {
            Object obj = params[i];
            int type = types[i] ;
            
			if (isNull(obj)) {
				pstmt.setNull(i + mediateLoc, type);
			} else if (isBlobType(type) && (obj instanceof InputStream)) {
				InputStream input = (InputStream) obj;
				try {
					byte[] bytes = IOUtils.toByteArray(input);
					pstmt.setBinaryStream(i + mediateLoc, new ByteArrayInputStream(bytes), bytes.length);
					// pstmt.setBytes(i+1, bytes) ;
				} catch (IOException e) {
					throw new SQLException(e.getMessage());
				} finally {
					IOUtils.closeQuietly(input);
				}
			} else if (isClobType(type) && (obj instanceof Reader)) {
				Reader reader = null;
				try {
					reader = (Reader) obj;
					Rope rope = RopeBuilder.build(reader);
					pstmt.setCharacterStream(i + mediateLoc, new RopeReader(rope), rope.length());

				} catch (IOException e) {
					throw new SQLException(e.getMessage());
				} finally {
					IOUtils.closeQuietly(reader);
				}
			} else if (isClobType(type)) {
				CharSequence cseq = (CharSequence) obj;
				Rope rope = RopeBuilder.build(cseq);
				pstmt.setCharacterStream(i + mediateLoc, new RopeReader(rope), rope.length());
			} else {
				pstmt.setObject(i + mediateLoc, obj);
			}
		}
	}

}
