package net.ion.framework.exception;

import java.util.Hashtable;
import java.util.Stack;

public class ExceptionManager {
	private static Hashtable<String, ExceptionManager> exceptionManagerCache = new Hashtable<String, ExceptionManager>();

	private String managerName = null;

	private Stack<Object> exceptionChain = new Stack<Object>();

	/**
	 * ExceptionManager 를 생성한다. Manager 이름을 parameter 로 받는다. Manager 는 ExceptionManagerFactory에서 getExceptionManager() 메소드로 생성한다.
	 * 
	 * @param managerName
	 *            String
	 */
	private ExceptionManager(String managerName) {
		this.managerName = managerName;
	}

	/**
	 * ExceptionManager 를 가져온다.
	 * 
	 * @param managerName
	 *            String 가져올 ExceptionManager 의 이름
	 * @return ExceptionManager
	 */
	public static ExceptionManager getExceptionManager(String managerName) {
		ExceptionManager manager = (ExceptionManager) (exceptionManagerCache.get(managerName));

		if (manager == null) {
			manager = new ExceptionManager(managerName);
			exceptionManagerCache.put(managerName, manager);
		}

		return manager;
	}

	/**
	 * ExceptionHandler 를 등록한다. 등록된 순서대로 exceptionChain 이 구성된다.
	 * 
	 * @param handler
	 *            ExceptionHandler
	 */
	public void addHandler(ExceptionHandler handler) {
		Object lastHandler = null;

		if (!exceptionChain.isEmpty()) {
			lastHandler = exceptionChain.pop();
			((ExceptionHandler) lastHandler).setNext(handler);
			exceptionChain.push(lastHandler);
		}

		exceptionChain.push(handler);
	}

	/**
	 * exception을 처리한다.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 */
	public void execHandler(ExceptionInfo exInfo) {
		((ExceptionHandler) exceptionChain.firstElement()).support(exInfo);
	}

	/**
	 * ExceptionManager의 이름을 가져온다.
	 * 
	 * @return String
	 */
	public String getName() {
		return managerName;
	}

	/**
	 * 등록되어있는 ExceptionHandler의 갯수를 가져온다.
	 * 
	 * @return int
	 */
	public int getChainSize() {
		return exceptionChain.size();
	}

	/**
	 * 등록되어있는 ExceptionHandler를 모두삭제한다.
	 * 
	 */
	public void clearHandler() {
		exceptionChain.clear();
	}

}
