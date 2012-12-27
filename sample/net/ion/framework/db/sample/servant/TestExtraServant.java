package net.ion.framework.db.sample.servant;

import java.io.IOException;
import java.util.List;

import org.apache.commons.chain.web.ChainServlet;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.sample.TestBaseSample;
import net.ion.framework.db.servant.AfterTask;
import net.ion.framework.db.servant.ChannelServant;
import net.ion.framework.db.servant.IExtraServant;
import net.ion.framework.db.servant.PrintOutServant;
import net.ion.framework.db.servant.ServantChain;
import net.ion.framework.db.servant.StdOutServant;
import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestExtraServant extends TestBaseSample{


	public void testFirst() throws Exception {
		
		ServantChain schain = new ServantChain().addServant(new PrintOutServant()).addServant(new PrintOutServant()) ;
		ChannelServant channel = new ChannelServant(new SlowServant(), new PrintOutServant()) ;
		schain.addServant(channel) ;
		
		IDBController newDc = new DBController("test", dc.getDBManager(), schain) ;
		
		newDc.getRows("select 1 from dual") ; 
	}

	private static class SlowServant implements IExtraServant{

		public void support(AfterTask atask) {
			try {
				Thread.sleep(2000) ;
				Debug.line() ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void testnCounter() throws Exception {
		ServantChain schain = new ServantChain() ;
		CounterServant counter = new CounterServant();
		schain.addServant(counter) ;
		
		IDBController newDc = new DBController("test", dc.getDBManager(), schain) ;
		newDc.initSelf() ;
		
		Rows rows = newDc.getRows("select * from dual") ;
		assertEquals(1, counter.getCount()) ;
	}
	
	
	public void testChannelServant() throws Exception {
		ChannelServant cs = new ChannelServant(IExtraServant.BLANK) ;
		cs.add(IExtraServant.BLANK) ;
		
		ServantChain schain = new ServantChain() ;
		schain.addServant(new CounterServant()) ;
		schain.addServant(cs) ;
		
		DBController newDc = new DBController("test", dc.getDBManager(), schain) ;
		
		assertEquals(true, newDc.getServantChain() == schain) ;
		List<IExtraServant> servants = newDc.getServantChain().getServants() ;
		assertEquals(3, servants.size()) ;
	}
	
	public void testChannelServant2() throws Exception {

		ChannelServant cs = new ChannelServant(IExtraServant.BLANK) ;
		cs.add(new ChannelServant(IExtraServant.BLANK)) ;
		cs.add(new ServantChain().addServant(IExtraServant.BLANK)) ;
		
		ServantChain schain = new ServantChain() ;
		schain.addServant(new CounterServant()) ;
		schain.addServant(cs) ;

		DBController newDc = new DBController("test", dc.getDBManager(), schain) ;

		List<IExtraServant> servants = newDc.getServantChain().getServants() ;
		assertEquals(4, servants.size()) ;
	}
	
	public void testChannelServant3() throws Exception {
		ChannelServant cs = new ChannelServant(IExtraServant.BLANK) ;
		cs.add(new ChannelServant(IExtraServant.BLANK)) ;
		
		ServantChain schain = new ServantChain() ;
		schain.addServant(new CounterServant()) ;
		schain.addServant(cs) ;
//		cs.add(schain) ;

		DBController newDc = new DBController("test", dc.getDBManager(), schain) ;

		List<IExtraServant> servants = newDc.getServantChain().getServants() ;
		assertEquals(3, servants.size()) ;
		
	}
	
	
	public void xtestExceptionServant() throws Exception {
		DBController newDc = new DBController("test", dc.getDBManager(), new ExceptionServant()) ;
		
		Rows rows = newDc.getRows("select * from dual" ) ;
		Debug.debug(rows) ;
	}
	

	public void testExceptionChannelServant() throws Exception {
		ChannelServant cs = new ChannelServant(new PrintOutServant(), new ExceptionServant()) ;
		DBController newDc = new DBController("test", dc.getDBManager(), cs) ;
		
		Rows rows = newDc.getRows("select * from dual" ) ;
		Debug.debug(rows) ;
	}
	
	public void testChannelThread() throws Exception {
		ChannelServant cs = new ChannelServant(11, new PrintOutServant()) ; // least 10
		DBController newDc = new DBController("test", dc.getDBManager(), cs) ;

		int count = 0 ;
		for (int i = 0; i < 12; i++) {
			Rows rows = newDc.getRows("select * from dual" ) ;
			count++ ;
		}
		
		assertEquals(12, count) ;
	}
	
	public void testChannelThread2() throws Exception {
		PrintOutServant std = new PrintOutServant();
		ChannelServant cs = new ChannelServant(std) ;
		DBController newDc = new DBController("test", dc.getDBManager(), cs) ;

		Debug.line(newDc.getServantChain().getServants().get(0)) ;
		
		assertEquals(true, std == newDc.getServantChain().getServants().get(0)) ;
		
	}
	
	public void testEmptyChannel() throws Exception {
		ChannelServant cs = new ChannelServant() ;
		DBController newDc = new DBController("test", dc.getDBManager(), cs) ;
		assertEquals(true, IExtraServant.BLANK == newDc.getServantChain().getServants().get(0)) ;
	}
}

class ExceptionServant implements IExtraServant{

	public void support(AfterTask atask) {
		throw new IllegalStateException() ;
	}
	
}

class CounterServant implements IExtraServant{

	private int count = 0 ;
	
	public void support(AfterTask atask) {
		count++ ;
	}
	
	public int getCount() {
		return count ;
	}
	
}
