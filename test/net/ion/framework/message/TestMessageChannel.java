package net.ion.framework.message;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;
import net.ion.framework.util.InfinityThread;

public class TestMessageChannel extends TestCase{

	
	public void testRunnable() throws Exception {
		
		MessageChannel channel = new MessageChannel(5) ;
		
		for (int i = 0; i < 5; i++) {
			channel.putMessage(new BlankMessage()) ;
		}
		
		
		new InfinityThread().startNJoin() ;
	}

}


class BlankMessage extends Message {

	public void handle() throws Exception {
		Debug.line() ;
	}
	
}