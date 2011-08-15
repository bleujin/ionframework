package net.ion.framework.schedule;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.StackTrace;

/**
 * 특정시각에 지정된 작업을 하도록 한다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class Scheduler extends Thread {
	private String name = null;

	private Hashtable<String, Job> jobs = null;
	private Logger logger = null;

	public Scheduler() {
		this("Anonymous scheduler(" + System.currentTimeMillis() + ")"); // 임의로 이름 만들어 준다.
	}

	/**
	 * 스케줄러를 생성한다.
	 * 
	 * @param name
	 *            String 스케줄러 이름
	 */
	public Scheduler(String name) {
		super(name);
		this.name = name;
		jobs = new Hashtable<String, Job>();
		logger = LogBroker.getLogger(this);
		this.setDaemon(true);
	}

	/**
	 * 새로운 작업을 추가한다. <br/>
	 * 주의: job의 name이 같을 경우 기존의 job이 교체된다.
	 * 
	 * @param job
	 *            추가할 job
	 */
	public synchronized void addJob(Job job) {
		this.jobs.put(job.getName(), job);
		logger.log(Level.INFO, "job added.:" + job);
	}

	/**
	 * job을 제거한다.
	 * 
	 * @param job
	 *            Job 제거할 job
	 */
	public void removeJob(Job job) {
		removeJob(job.getName());
	}

	/**
	 * job을 제거한다.
	 * 
	 * @param jobName
	 *            String 제거할 job 이름
	 */
	public synchronized void removeJob(String jobName) {
		Job job = getJob(jobName);
		logger.log(Level.INFO, "job removed.:" + job);
		this.jobs.remove(jobName);
	}

	/**
	 * 특정 job을 리턴
	 * 
	 * @param jobName
	 *            String job이름
	 * @return Job
	 */
	public Job getJob(String jobName) {
		return this.jobs.get(jobName);
	}

	/**
	 * 등록된 전체 job을 리턴한다.
	 * 
	 * @return Job[]
	 */
	public Job[] getJobs() {
		return this.jobs.values().toArray(new Job[0]);
	}

	/**
	 * 등록된 전체 job의 이름 목록을 리턴한다.
	 * 
	 * @return String[]
	 */
	public String[] getJobNames() {
		return this.jobs.keySet().toArray(new String[0]);
	}

	public String toString() {
		String[] jobNames = getJobNames();

		StringBuffer buff = new StringBuffer();

		buff.append("{name=" + this.name + ",jobs={");

		for (int i = 0; i < jobNames.length; ++i) {
			buff.append(jobNames[i]);
			if (i + 1 != jobNames.length) {
				buff.append(",");
			}
		}
		buff.append("}}");

		return buff.toString();
	}

	public void run() {
		logger.log(Level.INFO, "Scheduler started. :" + this.toString());

		int minute = 0;
		Calendar cal = null;

		try {
			while (!isInterrupted()) {
				cal = Calendar.getInstance();

				// System.out.println("####" + minute + " != "+
				// cal.get(Calendar.MINUTE));
				// 1분 마다 한번씩 jobs을 검색해서 실행한다.
				if (minute != cal.get(Calendar.MINUTE)) {
					minute = cal.get(Calendar.MINUTE);

					synchronized (this) {
						Enumeration<Job> je = jobs.elements();
						while (je.hasMoreElements()) {
							Job j = je.nextElement();
							AtTime at = j.getAtTime();
							Runnable r = j.getScheduledRunnable();

							if (at.match()) {
								try {
									RunnableThreadBox tb = new RunnableThreadBox(r);
									tb.setDaemon(true);
									tb.setPriority(j.getPriority());
									tb.start();
								} catch (Throwable t) {
									logger.log(Level.SEVERE, j.toString() + ":" + StackTrace.trace(t));
								}
							}

							if (at.wasExpired()) {
								removeJob(j);
							}
						}
					}
				}
				sleep(1000); // 1 sec
			}
		} catch (Exception e) {
		}

		logger.log(Level.INFO, "Scheduler stopped. :" + this.toString());
	}

	/**
	 * 스케줄러를 시작한다.
	 */
	public void start() {
		super.start();
	}

	/**
	 * 스케줄러를 종료한다.
	 */
	public void interrupt() {
		super.interrupt();
	}

	/**
	 * serialized 된 job 문자열을 job object로 가져온다.
	 * 
	 * @param serializedJobs
	 * @return
	 * @throws SerializedStringException
	 */
	// public Job[] deserializeJobs( String serializedJobs ) throws
	// SerializedStringException
	// {
	// Hashtable j=(Hashtable)SerializedString.deserialize(serializedJobs);
	// Job[] js=(Job[])j.values().toArray(new Job[0]);
	//
	// logger.log( Level.INFO, "jobs deserialized." ) ;
	// return js ;
	// }
	/**
	 * scheduler가 가지고 있는 jobs 을 String으로 serialization 한다.
	 * 
	 * @return
	 * @throws SerializedStringException
	 */
	// public String serializeJobs() throws SerializedStringException
	// {
	// String s=SerializedString.serialize(jobs);
	// logger.log(Level.INFO,"jobs serialized.");
	// return s;
	// }
	/**
	 * serialized된 job 문자열을 object로 복원하여 scheduler에 추가한다.
	 * 
	 * @param serializedJobs
	 * @throws SerializedStringException
	 */
	// public void loadSerializedJobs( String serializedJobs ) throws
	// SerializedStringException
	// {
	// Job[] js=deserializeJobs(serializedJobs);
	// for(int i=0;i < js.length;++i) {
	// addJob(js[i]);
	// }
	// }
}
