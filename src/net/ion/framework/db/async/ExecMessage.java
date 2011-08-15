package net.ion.framework.db.async;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class ExecMessage {

	final static String COMPLETE = "true";
	final static String PROGRESS = "..ing";
	final static String SUCCESS = "true";
	final static String FAIL = "false";
	final static String UNKNOWN = "unknown";

	final static ExecMessage SUCCESS_MESSAGE = new ExecMessage(COMPLETE, SUCCESS);
	final static ExecMessage FAIL_MESSAGE = new ExecMessage(COMPLETE, FAIL);
	final static ExecMessage PROGRESS_MESSAGE = new ExecMessage(PROGRESS, UNKNOWN);

	private String isComplete;
	private String isSuccess;

	private ExecMessage(String isComplete, String isSuccess) {
		this.isComplete = isComplete;
		this.isSuccess = isSuccess;
	}

	public String toString() {
		return "Complete : " + isComplete + ", Success : " + isSuccess;
	}

	public boolean isSuccess() {
		return SUCCESS.equals(isSuccess);
	}

	public boolean isComplete() {
		return COMPLETE.equals(isComplete);
	}

}
