package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class StdOracleParamUtils {
    public static final int COMMAND_LOC = 1 ;
    public static final int PROCEDURE_LOC = 2 ;

    private StdOracleParamUtils() {
    }

    static void setParam(Connection conn, PreparedStatement pstmt, List<Object> _param, List<Integer> _types, int mediateLoc) throws SQLException, IOException {
        Object[] params = (Object[])_param.toArray(new Object[0]) ;
        int[] types = ArrayUtils.toPrimitive((Integer[])_types.toArray(new Integer[0]) ) ;

        for(int i = 0, last = params.length; i < last; i++) {
            Object obj = params[i];
            int type = types[i];
            if(isNull(obj)) {
                pstmt.setNull(i + mediateLoc, types[i]);
            } else if(isClobType(type)) {
            	pstmt.setString(i + mediateLoc, (obj == null) ? "" : obj.toString() );
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

}