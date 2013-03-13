package net.ion.framework.db.servant;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import sun.security.krb5.internal.crypto.Des;

import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

public class ServantChain implements IExtraServant, GroupServant{

	private List<IExtraServant> servants = ListUtil.newList() ;
	
	public ServantChain addServant(IExtraServant newServant){
		synchronized (servants){
			servants.add(newServant) ;
			return this ;
		}
	}

	public List<IExtraServant> getServantList(){
		List<IExtraServant> result = ListUtil.newList() ;
		for (IExtraServant servant : servants) {
			result.addAll(decendantList(servant)) ;
		}
		return Collections.unmodifiableList(result) ;
	}
	
	private List<IExtraServant> decendantList(IExtraServant servant){
		List<IExtraServant> result = ListUtil.newList() ;
		if (servant instanceof GroupServant){
			for (IExtraServant inner : ((GroupServant)servant).getServantList()) {
				result.addAll(decendantList(inner)) ;
			}
		} else {
			result.add(servant) ;
		}
		
		return result ;
	}
	
	public void support(AfterTask atask) {
		synchronized (servants) {
			for (IExtraServant servant : servants) {
				servant.support(atask) ;
			}
		}
	}

	public void stopServant() {
		for (IExtraServant servant : servants) {
			if (servant instanceof GroupServant){
				((GroupServant)servant).stopServant() ;
			}
		}
	}
	
}
