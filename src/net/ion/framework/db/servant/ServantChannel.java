package net.ion.framework.db.servant;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

public class ServantChannel {

	private static final int MAX_MESSAGE = 2000;
	private final LinkedList<AfterTask> taskQueue;

	private ExtraServant topServant;
	private Logger log;

	// private volatile boolean shutDownRequest = false;

	public ServantChannel(ExtraServant servant) {
		this.taskQueue = new LinkedList<AfterTask>();

		topServant = servant;
		log = LogBroker.getLogger(this);

		if (topServant != null)
			log.info("init Channel : " + servant);
	}

	public void startWorker() {
		if (topServant == null)
			return;

		topServant.setName(topServant.toString());
		topServant.setDaemon(true);
		topServant.restart();
		if (!(topServant instanceof NoneServant)) {
			log.info("start Channel : " + topServant.toString());
		}
	}

	public synchronized void putTask(AfterTask atask) throws InterruptedException {
		if (topServant == null)
			return;

		while (getCount() >= MAX_MESSAGE) {
			wait();
		}
		taskQueue.addLast(atask);

		if (getCount() > (MAX_MESSAGE / 2)) {
			log.log(Level.WARNING, "Warning.. reach at the half of queue size.. if any exeption not exists, consider to increase queue's max size");
		}

		notifyAll();
	}

	public synchronized AfterTask takeTask() throws InterruptedException {
		while (getCount() <= 0) {
			wait();
		}

		AfterTask task = (AfterTask) taskQueue.removeFirst();

		notifyAll();
		return task;
	}

	private int getCount() {
		return taskQueue.size();
	}

	public synchronized void stopServant() {
		if (topServant == null)
			return;
		topServant.stopServant();

		if (topServant.getState().equals(Thread.State.RUNNABLE)) {
			log.info("Message Channel : destroyed");
		}
	}

	public ExtraServant getExtraServant() {
		return topServant;
	}

	public void restartWorker() {
		taskQueue.clear();
		stopServant();

		// 왜 다시 만들까 =ㅅ=??
		// ExtraServant clone = topServant.newCloneInstance();
		// clone.setChannel(this);
		// if (topServant.getNextServant() != null) {
		// clone.setNext(topServant.getNextServant());
		// }
		// topServant = clone;
		startWorker();
	}

	public void addServant(ExtraServant addServant) {

		if (topServant != null) {
			addServant.setChannel(this);
			addServant.setNext(topServant);
			this.topServant = addServant;
		} else { // (topServant == null)
			this.topServant = addServant;
			addServant.setChannel(this);
			// restartWorker() ;
		}
	}
}
