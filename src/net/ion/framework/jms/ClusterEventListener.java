package net.ion.framework.jms;

import java.util.EventListener;

public interface ClusterEventListener extends EventListener {
	public void notified(ClusterEvent event);
}
