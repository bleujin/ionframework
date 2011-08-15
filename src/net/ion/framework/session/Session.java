package net.ion.framework.session;

import java.util.Enumeration;

/**
 * Session Interface
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public interface Session {
	/**
	 * @return session�� ������ �ð�
	 */
	public long getCreationTime();

	/**
	 * @return session�� id
	 */
	public String getId();

	/**
	 * @return �ֱ� session attribute�� ���� ���� �ð�
	 */
	public long getLastAccessedTime();

	/**
	 * @return session�� ����ϴ� context
	 */
	public SessionContext getSessionContext();

	/**
	 * @return session�� inactive ���·� valid�� �ð�(��)
	 */
	public int getMaxInactiveInterval();

	/**
	 * session���� attribute�� �����´�. ���� ��� null�̴�.
	 * 
	 * @param name
	 *            ������ attribute�� key
	 * @return
	 */
	public Object getAttribute(String name);

	/**
	 * session�� attribute�� �����Ѵ�.
	 * 
	 * @param name
	 *            ������ attribute�� key
	 * @param value
	 *            ������ attribute
	 */
	public void setAttribute(String name, Object value);

	/**
	 * session���� attribute�� �����.
	 * 
	 * @param name
	 *            ������� �ϴ� attribute�� key
	 */
	public void removeAttribute(String name);

	/**
	 * session���� ��� attribute�� �����.
	 */
	public void removeAttributeAll();

	/**
	 * session�� ��� attribute�� �����´�.
	 * 
	 * @return
	 */
	public Enumeration<?> getAttributeNames();

	/**
	 * session�� attribute ���� ��� invalidate�Ѵ�.(�����.)
	 */
	public void invalidate();

	/**
	 * ���� ������ session���� ���� (set,get�� �ѹ��� �Ͼ� ���� ������ true)
	 * 
	 * @return
	 */
	public boolean isNew();

	/**
	 * session ����
	 * 
	 * @return
	 */
	public String getInfo();
}
