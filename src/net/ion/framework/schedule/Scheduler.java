package net.ion.framework.schedule;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.util.ThreadPool;

import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.StackTrace;

/**
 * Ư���ð��� ������ �۾��� �ϵ��� �Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class Scheduler extends Thread {
	private String name = null;
	private static IExecutor EXECUTOR = new IExecutor(5, 2) ;

	private Hashtable<String, Job> jobs = null;
	private Logger logger = null;

	public Scheduler() {
		this("Anonymous scheduler(" + System.currentTimeMillis() + ")"); // ���Ƿ� �̸� ����� �ش�.
		
	}

	/**
	 * �����ٷ��� ���Ѵ�.
	 * 
	 * @param name
	 *            String �����ٷ� �̸�
	 */
	public Scheduler(String name) {
		super(name);
		this.name = name;
		jobs = new Hashtable<String, Job>();
		logger = LogBroker.getLogger(this);
		this.setDaemon(true);
	}

	/**
	 * ���ο� �۾��� �߰��Ѵ�. <br/>
	 * ����: job�� name�� ���� ��� ������ job�� ��ü�ȴ�.
	 * 
	 * @param job
	 *            �߰��� job
	 */
	public synchronized void addJob(Job job) {
		this.jobs.put(job.getName(), job);
		logger.log(Level.INFO, "job added.:" + job);
	}

	/**
	 * job�� �����Ѵ�.
	 * 
	 * @param job
	 *            Job ������ job
	 */
	public void removeJob(Job job) {
		removeJob(job.getName());
	}

	/**
	 * job�� �����Ѵ�.
	 * 
	 * @param jobName
	 *            String ������ job �̸�
	 */
	public synchronized void removeJob(String jobName) {
		Job job = getJob(jobName);
		logger.log(Level.INFO, "job removed.:" + job);
		this.jobs.remove(jobName);
	}

	/**
	 * Ư�� job�� ����
	 * 
	 * @param jobName
	 *            String job�̸�
	 * @return Job
	 */
	public Job getJob(String jobName) {
		return this.jobs.get(jobName);
	}

	/**
	 * ��ϵ� ��ü job�� �����Ѵ�.
	 * 
	 * @return Job[]
	 */
	public Job[] getJobs() {
		return this.jobs.values().toArray(new Job[0]);
	}

	/**
	 * ��ϵ� ��ü job�� �̸� ����� �����Ѵ�.
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
				// 1�� ���� �ѹ� jobs�� �˻��ؼ� �����Ѵ�.
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
									EXECUTOR.runTask(r) ;
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
	 * �����ٷ��� �����Ѵ�.
	 */
	public void start() {
		super.start();
	}

	/**
	 * �����ٷ��� �����Ѵ�.
	 */
	public void interrupt() {
		super.interrupt();
	}

	/**
	 * serialized �� job ���ڿ��� job object�� �����´�.
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
	 * scheduler�� ������ �ִ� jobs �� String���� serialization �Ѵ�.
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
	 * serialized�� job ���ڿ��� object�� �����Ͽ� scheduler�� �߰��Ѵ�.
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
