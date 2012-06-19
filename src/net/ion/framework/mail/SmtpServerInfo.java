package net.ion.framework.mail;

/**
 * @author bleujin
 * @version 1.0
 */

public class SmtpServerInfo {
	private String smtpAddress;
	private int smtpPortNo;
	private String smtpUserId;
	private String smtpUserPwd;

	static SmtpServerInfo smtpInfo = new SmtpServerInfo();

	public static SmtpServerInfo getInstance() {
		return smtpInfo;
	}

	private SmtpServerInfo() {
		this.smtpAddress = "zerg.i-on.net";
		this.smtpPortNo = 25;

		this.smtpUserId = "wizest";
		this.smtpUserPwd = null;
	}

	public SmtpServerInfo(String smtpAddress, int smtpPortNo, String smtpUserId, String smtpUserPwd) {
		this.smtpAddress = smtpAddress;
		this.smtpPortNo = smtpPortNo;

		this.smtpUserId = smtpUserId;
		this.smtpUserPwd = smtpUserPwd.equals("") ? null : smtpUserPwd;
	}

	public String getSmtpAdderss() {
		return smtpAddress;
	}

	public int getSmtpPortNo() {
		return smtpPortNo;
	}

	public String getSmtpUserId() {
		return smtpUserId;
	}

	public String getSmtpUserPwd() {
		return smtpUserPwd;
	}

}
