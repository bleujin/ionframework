package net.ion.framework.exception;

/**
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 * 
 *          <pre>
 * 실제 Exception 을 처리하는 추상클래스
 * 
 * resolve , done, fail 은 ExceptionHandler 클래스를 상속받아 구현한다.
 * 
 */

public abstract class ExceptionHandler {
	protected ExceptionHandler next = null;

	/**
	 * 핸들러가 처리할수 없는 exception에 대해서 넘겨줄 next 핸들러를 설정한다.
	 * 
	 * @param nextHandler
	 *            ExceptionHandler
	 */
	final public void setNext(ExceptionHandler nextHandler) {
		this.next = nextHandler;
	}

	/**
	 * Handler 를 동작시키는 매소드. resolve 매소드를 이용해서 처리가능여부를 확인한후 처리가능하면 done 매소드를 실행하고 처리불가능하면 next 핸들러가 있는지를 확인해서 있으면 next 핸들러에게 넘겨주고 없다면 fail 매소드를 실행한다.
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
	 * ExceptionInfo의 정보를 보고 처리할수 있는지 없는지를 확인하는 코드를 구현한다.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 * @return boolean true - 현 핸들러에서 처리가능하면 , false - 처리불가능하면
	 */
	protected abstract boolean resolve(ExceptionInfo exInfo);

	/**
	 * 처리할수 있는 Exception 일때 해당 핸들러가 처리할 코드를 구현한다.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 */
	protected abstract void done(ExceptionInfo exInfo);

	/**
	 * 처리할수 없는 Exception 인데 next 핸들러가 존재않을때 처리할 코드들을 구현한다.
	 * 
	 * @param exInfo
	 *            ExceptionInfo
	 */
	protected void fail(ExceptionInfo exInfo) {
		exInfo.recordException();
	}

}
