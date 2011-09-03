package net.ion.framework.db.sample.performance;

import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.sample.TestBaseDB;
import net.ion.framework.util.Debug;
import net.ion.framework.util.RandomUtil;

import org.apache.commons.lang.time.StopWatch;


public class BatchInsertSpeed extends TestBaseDB {
	
	public void setUp() throws Exception {
		super.setUp() ;
		// dc.execUpdate("create table if not exists performance_sample(a int, b char(150), c varchar(50)) ;") ;
		// row length�� �뷫 150 - 200 ���̷�..
	}
	
	public void tearDown() throws Exception {
		dc.execUpdate("delete from performance_sample") ; // Space Resizing ������ ��ް� �ϱ� ���� delete��..
		// dc.execUpdate("truncate table performace_sample") ;
		super.tearDown() ;
	}
	
	/*
	 * �Ϲ����� insert�� �ּ� �Ǵ� 50 ms ���Ϸ� �۵��ؾ� �Ѵ�. 
	 */
	public void testCommandInsert() throws Exception {
		// required : 50 ms per 1 unit
		int unitCount = 100 ;
		
		StopWatch sw = new StopWatch() ;
		sw.start() ;
		for (int i = 0; i < unitCount; i++) {
			IUserCommand cmd = dc.createUserCommand("insert into performance_sample(a, b, c) values(?, ?, ?)") ;
			cmd.addParam(0, unitCount + i) ;
			cmd.addParam(1, "No." + (unitCount + i) + ".....") ;
			cmd.addParam(2, RandomUtil.nextRandomString(RandomUtil.nextRandomInt(10, 50), RandomUtil.NUMBER_CHAR_TYPE)) ;
			cmd.execUpdate() ;
		}
		Debug.debug(sw.getTime()) ;
		assertEquals(true, unitCount * 50 > sw.getTime()) ;
		sw.stop() ;
	}
	
	
	/*
	 * Batch Insert�� �ּ� �Ǵ� 1 ms ���Ϸ� �۵��ؾ� �Ѵ�.  
	 */
	public void testBatchInsert() throws Exception {
		
		// required : 1 ms per 1 unit
		int unitCount = 10000 ;
		int batchCount = 3 ; // 10000 * 10 = 100,000
		
		StopWatch sw = new StopWatch() ;
		sw.start() ;
		for (int k = 0; k < batchCount; k++) {
			IUserCommandBatch cmd = dc.createUserCommandBatch("insert into performance_sample(a, b, c) values(?, ?, ?)") ;
			for (int i = 0; i < unitCount; i++) {
				cmd.addBatchParam(0, k * unitCount + i) ;
				cmd.addBatchParam(1, "No." + (k * unitCount + i) + ".....") ;
				cmd.addBatchParam(2, RandomUtil.nextRandomString(RandomUtil.nextRandomInt(10, 50), RandomUtil.NUMBER_CHAR_TYPE)) ;
			}
			cmd.execUpdate() ;
			cmd.clearParam() ;
			assertEquals(true, unitCount > sw.getTime()) ;
			sw.stop() ;
			sw.reset() ;
			sw.start() ;
		}
		sw.stop() ;
	}

}
