package net.ion.framework.session;

import java.util.Enumeration;

/**
 * ���� session�� ����� ����
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public interface SessionContext {
	public Enumeration<?> getSessions();

	/**
	 * session�� �߰��Ѵ�.
	 * 
	 * @param session
	 */
	public void add(AbstractSession session);

	/**
	 * session�� �����Ѵ�.
	 * 
	 * @param sessionId
	 */
	public void remove(String sessionId);

	/**
	 * session id�� session�� ã�´�.
	 * 
	 * @param sessionId
	 * @return
	 */
	public AbstractSession find(String sessionId);

	/**
	 * ��ü session�� �����´�
	 * 
	 * @return
	 */
	public AbstractSession[] findSessions();

	/**
	 * session�� ��
	 * 
	 * @return
	 */
	public int size();

	/**
	 * session context implementation ������ ��´�.
	 * 
	 * @return
	 */
	public String getInfo();
}
