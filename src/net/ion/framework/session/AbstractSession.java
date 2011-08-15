package net.ion.framework.session;

import java.io.Serializable;

/**
 * Session Interface�� session�� ���� ���� �͸� ���� �Ǿ� �ִµ� �װ��� Ȯ���Ͽ� session�� setting �ϴ� ���� �߰�
 * 
 * SessionManager,SessionContext������ AbstractSession�� �̿��Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public abstract class AbstractSession implements Session, Serializable {
	/**
	 * @param time
	 *            session�� ������ �ð�
	 */
	public abstract void setCreationTime(long time);

	/**
	 * @param id
	 *            session�� id
	 */
	public abstract void setId(String id);

	/**
	 * ���������� ������ �ð�. '����' attribute�� ���� ��� get,set...�� �ǹ��Ѵ�.
	 * 
	 * @param time
	 */
	public abstract void setLastAccessedTime(long time);

	/**
	 * @param context
	 *            session�� ����ϴ� context
	 */
	public abstract void setSessionContext(SessionContext context);

	/**
	 * @param interval
	 *            session�� inactive ���·� valid�� �ð�(��)
	 */
	public abstract void setMaxInactiveInterval(int interval);

	/**
	 * ���� ������ session���� ����
	 * 
	 * @param isNew
	 */
	public abstract void setNew(boolean isNew);
}
