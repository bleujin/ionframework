package net.ion.framework.pool;

import java.util.LinkedList;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.StackTrace;

/**
 * thread를 빈번하게 생성시키는 작업은 시스템에 부담을 많이주므로 pooling할 경우 효과적으로 성능을 향상시킬 수 있다. 미리 정한 수 만큼 thread를 생성하여 준비 상태로 있다가 thread의 요구에 따라 준비된 thread를 리턴하여 새로운 thread의 생성을 막고 사용이 끝나면 pool에 반환하여 다음에 계속사용한다.
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
	 *            String pool 이름
	 * @param numberOfThreads
	 *            int 초기 생성할 thread 수
	 * @param threadPriority
	 *            int thread 우선 순위 (1~10, 클수록 높다)
	 * @param daemonThread
	 *            boolean daemonThread 여부 (true if 프로그램이 종료될 경우 현재 thread의 동작 여부에 상관없이 정지함,false if 현 thread가 완전히 종료될 때 까지 main thread가 blocking된다.)
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
	 * pool을 종료한다.
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
	 * targer runnable을 실행한다.<br/>
	 * (주의: 즉시 실행하지 않고 대기큐에 넣어 처리된다.)
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
	 * pool 이름
	 * 
	 * @return String
	 */
	public String getName() {
		return this.poolName;
	}

	/**
	 * 대기 runnable 수
	 * 
	 * @return int
	 */
	public int getWaitingLineLength() {
		synchronized (this.waitingLine) {
			return this.waitingLine.size();
		}
	}

	/**
	 * @return thread pool 에서 실행 중인 runnable 객체를 리턴한다. (null이 포함될 수 있다.)
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
							waitingLine.wait(1000 * 60); // 1분에 한번씩 깨어난다. 혹시나 dead lock 걸릴까 하는 염려 ㅡ.ㅡ"
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
