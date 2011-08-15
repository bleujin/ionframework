package net.ion.framework.exception;

/**
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 * 
 *          <pre>
 * ���� Exception �� ó���ϴ� �߻�Ŭ����
 * 
 * resolve , done, fail �� ExceptionHandler Ŭ������ ��ӹ޾� �����Ѵ�.
 * 
 */

public abstract class ExceptionHandler {
	protected ExceptionHandler next = null;

	/**
	 * �ڵ鷯�� ó���Ҽ� ���� exception�� ���ؼ� �Ѱ��� next �ڵ鷯�� �����Ѵ�.
	 * 
	 * @param nextHandler
	 *            ExceptionHandler
	 */
	final public void setNext(ExceptionHandler nextHandler) {
		this.next = nextHandler;
	}

	/**
	 * Handler �� ���۽�Ű�� �żҵ�. resolve �żҵ带 �̿��ؼ� ó�����ɿ��θ� Ȯ������ ó�������ϸ� done �żҵ带 �����ϰ� ó���Ұ����ϸ� next �ڵ鷯�� �ִ����� Ȯ���ؼ� ������ next �ڵ鷯���� �Ѱ��ְ� ���ٸ� fail �żҵ带 �����Ѵ�.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 */
	final public void support(ExceptionInfo exInfo) {
		if (resolve(exInfo)) {
			done(exInfo);
		} else if (next != null) {
			next.support(exInfo);
		} else {
			fail(exInfo);
		}
	}

	/**
	 * ExceptionInfo�� ������ ���� ó���Ҽ� �ִ��� �������� Ȯ���ϴ� �ڵ带 �����Ѵ�.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 * @return boolean true - �� �ڵ鷯���� ó�������ϸ� , false - ó���Ұ����ϸ�
	 */
	protected abstract boolean resolve(ExceptionInfo exInfo);

	/**
	 * ó���Ҽ� �ִ� Exception �϶� �ش� �ڵ鷯�� ó���� �ڵ带 �����Ѵ�.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 */
	protected abstract void done(ExceptionInfo exInfo);

	/**
	 * ó���Ҽ� ���� Exception �ε� next �ڵ鷯�� ��������� ó���� �ڵ���� �����Ѵ�.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 */
	protected void fail(ExceptionInfo exInfo) {
		exInfo.recordException();
	}

}
