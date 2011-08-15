package net.ion.framework.message;

import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

/**
 * 하나이상의 메세지를 처리하기 위한 작업클래스. 하나이상의 메세지를 포함하며 WorkerThread를 이용하여 메세지를 처리한다.
 * 
 * @author bleujin
 * @version 1.0
 */

public class MessageChannel {
	private static final int MAX_MESSAGE = 1000;
	private final Message[] messageQueue;

	private int tail;
	private int head;
	private int count;

	private final WorkerThread[] threadPool;
	Logger log;

	private boolean isCompleted = false;

	public MessageChannel(int threads) {
		this.messageQueue = new Message[MAX_MESSAGE];
		this.head = 0;
		this.tail = 0;
		this.count = 0;

		threadPool = new WorkerThread[threads];
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i] = new WorkerThread("Message Worker-" + i, this);
		}
		log = LogBroker.getLogger(this);
	}

	/**
	 * WorkerThread를 실행시킨다.
	 */
	public void startWorker() {
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i].start();
		}
	}

	/**
	 * 처리할 메세지를 등록한다.
	 * 
	 * @param message
	 *            Message
	 */
	public synchronized void putMessage(Message message) {
		while (count >= messageQueue.length) {
			try {
				wait();
			} catch (InterruptedException ex) {
			}
		}
		messageQueue[tail] = message;
		tail = (tail + 1) % messageQueue.length;
		count++;

		notifyAll();
	}

	/**
	 * 등록되어 있는 메세지를 Queue에서 하나 가져온다.
	 * 
	 * @return Message
	 */
	public synchronized Message takeMessage() {
		while (count <= 0) {
			try {
				wait();
			} catch (InterruptedException ex) {
			}
		}

		Message message = messageQueue[head];
		head = (head + 1) % messageQueue.length;

		count--;
		notifyAll();
		return message;
	}

	/**
	 * 현재 Queue에 존재하는 처리할 메세지 갯수
	 * 
	 * @return int
	 */
	public int getCount() {
		return count;
	}

	public synchronized void destroy() {
		isCompleted = true;
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i].interrupt();
		}
		log.info("Message Channel : destroyed");
	}

	/**
	 * 메세지처리가 모두 완료 되었는가 확인
	 * 
	 * @return boolean
	 */
	public boolean isCompleted() {
		return isCompleted;
	}

}
