package net.ion.framework.schedule;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;
import net.ion.framework.util.InfinityThread;
import net.ion.framework.util.ListUtil;
import sun.management.ManagementFactory;

public class TestScheduler extends TestCase{

	public void testHang() throws Exception {
		Scheduler sche = new Scheduler() ;
		
		String crontab = "0-59/1 * * * * * * *";
		for (int i : ListUtil.rangeNum(50)) {
			Job job = new Job("my" + i, new ScheduledRunnable() {
				public void run() {
					Debug.line(getName());
				}
			}, new AtTime(crontab));
			sche.addJob(job) ;
			Thread.sleep(50) ;
		}
		sche.start() ;
		
		new Thread(){
			public void run(){
				while(true){
					try {
						Debug.line(ManagementFactory.getThreadMXBean().getThreadCount()) ;
						Thread.sleep(500) ;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start() ;
		
		new InfinityThread().startNJoin() ;
	}
}
