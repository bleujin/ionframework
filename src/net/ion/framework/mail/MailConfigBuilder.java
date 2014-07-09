package net.ion.framework.mail;

import java.util.Properties;

public class MailConfigBuilder {

	private Properties props;
	private ReceiveConfigBuilder receiveBuilder;
	private SendConfigBuilder sendBuilder ;

	MailConfigBuilder(Properties props) {
		this.props = props ;
		this.receiveBuilder = new ReceiveConfigBuilder(this) ;
		this.sendBuilder = new SendConfigBuilder(this) ;
	}

	public final static MailConfigBuilder create(){
		Properties props = System.getProperties() ;
		return new MailConfigBuilder(props) ;
	}
	
	public ReceiveConfigBuilder receiveConfig() {
		return receiveBuilder;
	}
	
	public SendConfigBuilder sendConfig(){
		return sendBuilder ;
	}

	
	
	public MailConfig buildConfig(){
		return new MailConfig(this.props, this.sendBuilder, this.receiveBuilder) ;
	}


}

