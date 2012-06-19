package net.ion.framework.jms;

import java.io.Serializable;

abstract public class ClusterMessage implements Serializable {
	public ClusterMessage() {
	}

	abstract public String getCommand();

	abstract public Serializable getData();

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("command=").append(getCommand());
		buf.append(", data=").append(getData());

		return buf.toString();
	}
}
