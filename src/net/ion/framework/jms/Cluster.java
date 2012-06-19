package net.ion.framework.jms;

import javax.swing.event.EventListenerList;

abstract public class Cluster {
	protected EventListenerList listenerList = new EventListenerList();
	public final static Cluster NONE_CLUSTER = new NoneCluster();

	// 자신을 제외한 노드들로 전송
	abstract public void sendMessage(ClusterMessage message);

	// 자신을 포함한 모든 노드로 전송
	abstract public void sendMessageAll(ClusterMessage message);

	public void addClusterEventListener(ClusterEventListener listener) {
		listenerList.add(ClusterEventListener.class, listener);
	}

	public void removeClusterEventListener(ClusterEventListener listener) {
		listenerList.remove(ClusterEventListener.class, listener);
	}

	protected void dispatchClusterEvent(ClusterEvent event) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ClusterEventListener.class) {
				((ClusterEventListener) listeners[i + 1]).notified(event);
			}
		}
	}
}
