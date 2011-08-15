package net.ion.framework.message;

/**
 * 사용안함.
 * 
 * @author bleujin
 * @version 1.0
 */

public class OutMessage extends Message {

	OutMessageInfo outMessageInfo;

	public OutMessage(String name, OutMessageInfo outMessageInfo) {
		this.outMessageInfo = outMessageInfo;
	}

	public void handle() throws Exception {
		outMessageInfo.sendOut();
	}

}
