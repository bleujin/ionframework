package net.ion.framework.mail;


public class SendConfigBuilder {


	private MailConfigBuilder parent ;
	private String server = "smtp.i-on.net";
	private int portNum = 25;
	private String mailId;
	private String password;
	

	SendConfigBuilder(MailConfigBuilder parent) {
		this.parent = parent ;
	}

	public String serverHost(){
		return server;
	}
	
	public String mailUserId(){
		return mailId ;
	}
	
	public String mailUserPwd(){
		return password ;
	}
	
	public int serverPort(){
		return portNum ;
	}
	
	
	public SendConfigBuilder server(String serverHost){
		this.server = serverHost ;
		return this ;
	}
	
	public SendConfigBuilder mailUserId(String mailId){
		this.mailId = mailId ;
		return this ;
	}
	
	public SendConfigBuilder mailUserPwd(String password){
		this.password = password ;
		return this ;
	}

	public SendConfigBuilder port(int portNum) {
		this.portNum = portNum ;
		return this;
	}

	public MailConfig buildConfig() {
		return parent.buildConfig() ;
	}

	public ReceiveConfigBuilder receiveConfig() {
		return parent.receiveConfig();
	}


}
