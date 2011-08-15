package net.ion.framework.message;

import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

/**
 * �ϳ��̻��� �޼����� ó���ϱ� ���� �۾�Ŭ����. �ϳ��̻��� �޼����� �����ϸ� WorkerThread�� �̿��Ͽ� �޼����� ó���Ѵ�.
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
	 * WorkerThread�� �����Ų��.
	 */
	public void startWorker() {
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i].start();
		}
	}

	/**
	 * ó���� �޼����� ����Ѵ�.
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
	 * ��ϵǾ� �ִ� �޼����� Queue���� �ϳ� �����´�.
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
	 * ���� Queue�� �����ϴ� ó���� �޼��� ����
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
	 * �޼���ó���� ��� �Ϸ� �Ǿ��°� Ȯ��
	 * 
	 * @return boolean
	 */
	public boolean isCompleted() {
		return isCompleted;
	}

}
