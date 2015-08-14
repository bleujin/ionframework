package net.ion.framework.db.servant;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import net.ion.framework.util.ListUtil;
import net.ion.framework.util.WithinThreadExecutor;

public class ServantChain implements IExtraServant, GroupServant {

	private List<IExtraServant> servants = ListUtil.newList();
	private ExecutorService es = null;

	public ServantChain(){
		this(new WithinThreadExecutor()) ;
	}
	public ServantChain(ExecutorService es) {
		this.es = es ;
	}


	public ServantChain addServant(IExtraServant newServant) {
		synchronized (servants) {
			servants.add(newServant);
			return this;
		}
	}

	public List<IExtraServant> getServantList() {
		List<IExtraServant> result = ListUtil.newList();
		for (IExtraServant servant : servants) {
			result.addAll(decendantList(servant));
		}
		return Collections.unmodifiableList(result);
	}

	public ServantChain eservice(ExecutorService es){
		shutdownEService(); 
		this.es = es ;
		return this ;
	}
	
	private List<IExtraServant> decendantList(IExtraServant servant) {
		List<IExtraServant> result = ListUtil.newList();
		if (servant instanceof GroupServant) {
			for (IExtraServant inner : ((GroupServant) servant).getServantList()) {
				result.addAll(decendantList(inner));
			}
		} else {
			result.add(servant);
		}

		return result;
	}

	public void support(final AfterTask atask) {
		es.submit(new Callable<Void>() {
			public Void call() throws Exception {
				for (IExtraServant servant : servants) {
					servant.support(atask);
				}
				return null;
			}
		});
	}

	public void stopServant() {
		for (IExtraServant servant : servants) {
			if (servant instanceof GroupServant) {
				((GroupServant) servant).stopServant();
			}
		}
		shutdownEService();
	}

	private void shutdownEService() {
		List<Runnable> runnable = es.shutdownNow();
		for (Runnable run : runnable) {
			run.run();
		}
	}

}
