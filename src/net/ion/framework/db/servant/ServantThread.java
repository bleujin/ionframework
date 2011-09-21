package net.ion.framework.db.servant;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import net.ion.framework.util.ListUtil;

public class ServantThread extends Thread {

	private ChannelServant channel;
	private List<IExtraServant> children;

	public ServantThread(IExtraServant[] servants, ChannelServant channel) {
		super("ServantThread");
		this.children = ListUtil.syncList(servants);
		if (servants.length == 0) this.children.add(IExtraServant.BLANK);
		this.channel = channel;
	}

	synchronized void add(IExtraServant newServant) {
		children.add(newServant);
	}

	public void run() {
		while (true) {
			try {
				AfterTask atask = channel.takeEvent();

				synchronized (this) {
					for (IExtraServant servant : children) {
						servant.support(atask);
					}
				}
			} catch (InterruptedException ignore) {
				ignore.printStackTrace();
			}

		}
	}

	public List<IExtraServant> getServantList() {
		return children ;
	}
}
