package net.ion.framework.template;

/**
 * TemplateRuntime �� page�� generationg �� �Ŀ� �ϼ��� text�� �Ѱ��� �� ���
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public interface PageReceiver {
	/**
	 * TemplateRuntime�� ���� �ҷ�����.
	 * 
	 * @param page
	 *            Page ������ page
	 */
	void receivePage(Page page);
}
