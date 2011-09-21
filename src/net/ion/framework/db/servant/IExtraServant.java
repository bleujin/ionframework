package net.ion.framework.db.servant;

public interface IExtraServant {

	public final static IExtraServant BLANK = new NoneServant() ; 
	
	public void support(AfterTask atask)  ;
}
