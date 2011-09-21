package net.ion.framework.db.servant;

import java.util.Collections;
import java.util.List;

import net.ion.framework.util.ListUtil;

public class ServantChain implements IExtraServant{

	private List<IExtraServant> servants = ListUtil.newList() ;
	
	public ServantChain addServant(IExtraServant newServant){
		servants.add(newServant) ;
		return this ;
	}

	public List<IExtraServant> getServants(){
		List<IExtraServant> result = ListUtil.newList() ;
		for (IExtraServant servant : servants) {
			if (servant instanceof ChannelServant) {
				result.addAll(((ChannelServant)servant).getServantList()) ;
			} else if (servant instanceof ServantChain) {
				result.addAll(((ServantChain)servant).getServants()) ;
			} else {
				result.add(servant) ;
			}

		}
		
		return Collections.unmodifiableList(result) ;
	}
	
	public void support(AfterTask atask) {
		for (IExtraServant servant : servants) {
			servant.support(atask) ;
		}
	}
	
}
