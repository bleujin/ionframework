package net.ion.framework.mte;

import java.util.List;

import net.ion.framework.mte.util.NestedParser;
import net.ion.framework.util.ListUtil;

public class FunArgumentParser {

	private static FunArgumentParser SELF = new FunArgumentParser() ;
	
	private NestedParser mparser;
	private FunArgumentParser(){
		this.mparser = NestedParser.createOnlyMini() ;
	}
	
	public static FunArgumentParser getInstance() {
		return SELF ;
	}

	public Object[] parseArgs(String input) {
		List<Object> presult = mparser.parse(input, new String[]{"()", ","});
		
		if (presult.size() <=1) return (Object[])null ;
		
		Object argsExpr = presult.get(1);
		String[] argString = (List.class.isInstance(argsExpr)) ? ((List<String>)argsExpr).toArray(new String[0]) : new String[]{argsExpr.toString()} ;

		List<Object> result = ListUtil.newList() ; 
		for(String arg : argString){
			String trimedArg = arg.trim();
			if(trimedArg.startsWith("\"")) result.add(trimedArg.substring(1, trimedArg.length()-1)) ;
			else result.add(Integer.parseInt(trimedArg)) ;
		}
		
		return result.toArray();
	}

}
