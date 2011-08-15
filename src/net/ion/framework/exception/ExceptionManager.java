package net.ion.framework.exception;

import java.util.Hashtable;
import java.util.Stack;

public class ExceptionManager {
	private static Hashtable<String, ExceptionManager> exceptionManagerCache = new Hashtable<String, ExceptionManager>();

	private String managerName = null;

	private Stack<Object> exceptionChain = new Stack<Object>();

	/**
	 * ExceptionManager �� �����Ѵ�. Manager �̸��� parameter �� �޴´�. Manager �� ExceptionManagerFactory���� getExceptionManager() �޼ҵ�� �����Ѵ�.
	 * 
	 * @param managerName
	 *            String
	 */
	private ExceptionManager(String managerName) {
		this.managerName = managerName;
	}

	/**
	 * ExceptionManager �� �����´�.
	 * 
	 * @param managerName
	 *            String ������ ExceptionManager �� �̸�
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
	 * ExceptionHandler �� ����Ѵ�. ��ϵ� ������� exceptionChain �� �����ȴ�.
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
	 * exception�� ó���Ѵ�.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 */
	public void execHandler(ExceptionInfo exInfo) {
		((ExceptionHandler) exceptionChain.firstElement()).support(exInfo);
	}

	/**
	 * ExceptionManager�� �̸��� �����´�.
	 * 
	 * @return String
	 */
	public String getName() {
		return managerName;
	}

	/**
	 * ��ϵǾ��ִ� ExceptionHandler�� ������ �����´�.
	 * 
	 * @return int
	 */
	public int getChainSize() {
		return exceptionChain.size();
	}

	/**
	 * ��ϵǾ��ִ� ExceptionHandler�� ��λ����Ѵ�.
	 * 
	 */
	public void clearHandler() {
		exceptionChain.clear();
	}

}
