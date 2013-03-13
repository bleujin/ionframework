package net.ion.framework.db.servant;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import org.apache.ecs.xhtml.li;

import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

public class AsyncServant implements IExtraServant, GroupServant {

	private List<IExtraServant> listHandler = ListUtil.newList();
	private ExecutorService es;

	public AsyncServant(ExecutorService es, IExtraServant... handler) {
		this.es = es;
		for (IExtraServant s : handler) {
			listHandler.add(s);
		}
	}

	public AsyncServant add(IExtraServant newServant) {
		listHandler.add(newServant);
		return this;
	}

	public List<IExtraServant> getServantList() {
		return Collections.unmodifiableList(listHandler);
	}

	public void support(final AfterTask event) {
		es.submit(new Callable<Boolean>() {
			public Boolean call() throws Exception {
				for (IExtraServant servant : listHandler) {
					servant.support(event);
				}
				return true;
			}
		});
	}

	public void stopServant() {
		es.shutdown();
	}

}
