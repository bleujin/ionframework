package net.ion.framework.db.servant;

public abstract class AbstractExtraServant implements IExtraServant{

	public void support(AfterTask atask) {
		if (isDealWith(atask)){
			handle(atask) ;
		}
	}
	
	protected abstract boolean isDealWith(AfterTask atask) ;
	protected abstract void handle(AfterTask atask) ;

}
