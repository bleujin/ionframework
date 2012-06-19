package net.ion.framework.message;


import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

public abstract class Message implements Runnable, IMessage{
	private final String name;
	private Logger log = LogBroker.getLogger(Message.class) ;
	
	public Message() {
		this("None");
	}

	public Message(String name) {
		this.name = name;
	}


	public void run(){
		try {
			handle() ;
		} catch (Exception ignore) {
			log.warning(ignore.toString()) ;
		}
	}
	
	public String getName() {
		return name;
	}

	public String toString() {
		return "[ Message from " + getName() + " ]";
	}
}
