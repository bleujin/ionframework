package net.ion.framework.mail;

import java.util.Properties;

import net.ion.framework.mail.ReceiveConfigBuilder.Protocol;
import net.ion.framework.util.StringUtil;

public class MailConfig {

	private final Properties props;
	
	private final String sendServerHost;
	private final int sendServerPort;
	private final String sendMailUserId;
	private final String sendMailUserPwd;
	
	private final String recServerHost;
	private final int recServerPort;
	private final String recMailUserId;
	private final String recMailUserPwd;
	private final Protocol recPortocol;

	MailConfig(Properties props, SendConfigBuilder sendConfigBuilder, ReceiveConfigBuilder receiveConfigBuilder) {
		this.props = props ;
		
		this.sendServerHost = sendConfigBuilder.serverHost() ;
		this.sendServerPort = sendConfigBuilder.serverPort() ;
		this.sendMailUserId = sendConfigBuilder.mailUserId() ;
		this.sendMailUserPwd = sendConfigBuilder.mailUserPwd() ;
		
		
		this.recServerHost = receiveConfigBuilder.serverHost() ;
		this.recServerPort = receiveConfigBuilder.serverPort() ;
		this.recMailUserId = receiveConfigBuilder.mailUserId() ;
		this.recMailUserPwd = receiveConfigBuilder.mailUserPwd() ;
		this.recPortocol = receiveConfigBuilder.protocol() ;
		
	}

	public Properties prop() {
		return props;
	}

	
	public String sendServerHost() {
		return sendServerHost;
	}

	public int sendServerPort() {
		return sendServerPort;
	}

	public String sendMailUserId() {
		return sendMailUserId;
	}

	public String sendMailUserPwd() {
		return sendMailUserPwd;
	}

	
	
	
	
	public String recServerHost() {
		return recServerHost;
	}

	public int recServerPort() {
		return recServerPort;
	}

	public String recMailUserId() {
		return recMailUserId;
	}

	public String recMailUserPwd() {
		return recMailUserPwd;
	}

	public Protocol recPortocol() {
		return recPortocol;
	}
	
	

	public MailConfig confirmValidOfSendMailConfig(){
		if (StringUtil.isBlank(sendMailUserId)) throw new IllegalStateException("sendConfg : UserId is blank") ; 
		if (StringUtil.isBlank(sendMailUserPwd)) throw new IllegalStateException("sendConfg : UserPwd is blank") ; 
		if (StringUtil.isBlank(sendServerHost)) throw new IllegalStateException("sendConfg : serverHost is blank") ; 
		if (sendServerPort <= 0) throw new IllegalStateException("sendConfg : illegal portNum") ;
		return this ;
	}
	
	

	public MailConfig confirmValidOfReceiveMailConfig(){
		if (StringUtil.isBlank(recMailUserId)) throw new IllegalStateException("receiveConfg : UserId is blank") ; 
		if (StringUtil.isBlank(recMailUserPwd)) throw new IllegalStateException("receiveConfg : UserPwd is blank") ; 
		if (StringUtil.isBlank(recServerHost)) throw new IllegalStateException("receiveConfg : serverHost is blank") ; 
		if (recServerPort <= 0) throw new IllegalStateException("receiveConfg : illegal portNum") ;
		return this ;
	}

	public Mailer createMailer() {
		return new Mailer(this);
	}

	
}
