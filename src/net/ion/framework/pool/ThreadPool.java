package net.ion.framework.pool;

import java.util.LinkedList;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.StackTrace;

/**
 * thread�� ����ϰ� ������Ű�� �۾��� �ý��ۿ� �δ��� �����ֹǷ� pooling�� ��� ȿ�������� ������ ����ų �� �ִ�. �̸� ���� �� ��ŭ thread�� �����Ͽ� �غ� ���·� �ִٰ� thread�� �䱸�� ���� �غ�� thread�� �����Ͽ� ���ο� thread�� ������ ���� ����� ������ pool�� ��ȯ�Ͽ� ������ ��ӻ���Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class ThreadPool {
	private String poolName;

	private LinkedList<Runnable> waitingLine;
	private RunningSuite[] suites;

	int threadPriority;
	boolean daemonThread;

	private int threadUid;

	/**
	 * @param poolName
	 *            String pool �̸�
	 * @param numberOfThreads
	 *            int �ʱ� ������ thread ��
	 * @param threadPriority
	 *            int thread �켱 ���� (1~10, Ŭ���� ����)
	 * @param daemonThread
	 *            boolean daemonThread ���� (true if ���α׷��� ����� ��� ���� thread�� ���� ���ο� ������� ������,false if �� thread�� ������ ����� �� ���� main thread�� blocking�ȴ�.)
	 */
	public ThreadPool(String poolName, int numberOfThreads, int threadPriority, boolean daemonThread) {
		this.poolName = poolName;
		numberOfThreads = Math.max(1, numberOfThreads);

		this.waitingLine = new LinkedList<Runnable>();

		this.threadPriority = threadPriority;
		this.daemonThread = daemonThread;

		this.threadUid = 0;
		this.suites = new RunningSuite[numberOfThreads];
		for (int i = 0; i < numberOfThreads; ++i) {
			this.suites[i] = new RunningSuite();
		}
	}

	/**
	 * pool�� �����Ѵ�.
	 */
	public synchronized void destroy() {
		for (int i = 0; i < suites.length; ++i) {
			this.suites[i].stopSuite();
		}

		// give runners a quick chance to die.
		try {
			Thread.sleep(250);
		} catch (InterruptedException ex) {
		}
	}

	/**
	 * targer runnable�� �����Ѵ�.<br/>
	 * (����: ��� �������� �ʰ� ���ť�� �־� ó���ȴ�.)
	 * 
	 * @param target
	 *            Runnable
	 */
	public void execute(Runnable target) {
		synchronized (this.waitingLine) {
			this.waitingLine.add(target);
			this.waitingLine.notify();
		}
	}

	/**
	 * pool �̸�
	 * 
	 * @return String
	 */
	public String getName() {
		return this.poolName;
	}

	/**
	 * ��� runnable ��
	 * 
	 * @return int
	 */
	public int getWaitingLineLength() {
		synchronized (this.waitingLine) {
			return this.waitingLine.size();
		}
	}

	/**
	 * @return thread pool ���� ���� ���� runnable ��ü�� �����Ѵ�. (null�� ���Ե� �� �ִ�.)
	 */
	public Runnable[] _getCurrentTargets() {
		Runnable[] rs = new Runnable[suites.length];
		for (int i = 0; i < suites.length; ++i) {
			rs[i] = suites[i].getCurrentTarget();
		}
		return rs;
	}

	class RunningSuite {
		private Thread internalThread;
		private Runnable target;

		private RunningSuite() {
			Runnable r = new Runnable() {
				public void run() {
					try {
						invokeRunnable();
					} catch (Throwable t) {
						Logger logger = LogBroker.getLogger(this);
						logger.severe(StackTrace.trace(t));
					}
				}
			};

			this.internalThread = new Thread(r, getName() + "-" + ++threadUid);
			this.internalThread.setDaemon(daemonThread);
			this.internalThread.setPriority(threadPriority);
			this.internalThread.start();
		}

		@SuppressWarnings("static-access")
		private void invokeRunnable() {
			try {
				while (!internalThread.interrupted()) {
					try {
						target = null;
						synchronized (waitingLine) {
							target = (Runnable) waitingLine.removeFirst();
						}
						try {
							target.run();
						} catch (Throwable t) {
							t.printStackTrace();
						}
						target = null;
					} catch (Exception ne) {
						synchronized (waitingLine) {
							waitingLine.wait(1000 * 60); // 1�п� �ѹ��� �����. Ȥ�ó� dead lock �ɸ��� �ϴ� ���� ��.��"
						}
					}
				}
			} catch (InterruptedException ie) {
			}
		}

		private void stopSuite() {
			this.internalThread.interrupt();
		}

		private Runnable getCurrentTarget() {
			return this.target;
		}
	}
}
