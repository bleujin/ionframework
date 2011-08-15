package net.ion.framework.jms;

public class ClusterEvent {
	private Cluster cluster;
	private ClusterMessage message;

	public ClusterEvent(Cluster cluster, ClusterMessage message) {
		this.cluster = cluster;
		this.message = message;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public ClusterMessage getMessage() {
		return message;
	}
}
