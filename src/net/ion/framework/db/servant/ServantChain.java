package net.ion.framework.db.servant;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

public class ServantChain implements IExtraServant{

	private List<IExtraServant> servants = ListUtil.newList() ;
	
	public ServantChain addServant(IExtraServant newServant){
		synchronized (servants){
			servants.add(newServant) ;
			return this ;
		}
	}

	public List<IExtraServant> getServantList(){
		return Collections.unmodifiableList(servants) ;
	}
	
	public void support(AfterTask atask) {
		synchronized (servants) {
			for (IExtraServant servant : servants) {
				servant.support(atask) ;
			}
		}
	}
	
}
