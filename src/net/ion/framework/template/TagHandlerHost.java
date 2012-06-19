package net.ion.framework.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ion.framework.template.tagext.Tag;
import net.ion.framework.template.tagext.TagInfo;
import net.ion.framework.template.tagext.TagLibraryInfo;

/**
 * tagName�� �ش��ϴ� tag handling object �� �����Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class TagHandlerHost {
	private TagLibraryInfo tagLibraryInfo = null;

	private Map<String, Class<?>> handlerClassCache = null;

	/**
	 * @param tagLibraryInfo
	 * @param maxHandler
	 *            �� handler ���� �ִ� ũ��
	 */
	public TagHandlerHost(TagLibraryInfo tagLibraryInfo, int maxHandler) {
		this.tagLibraryInfo = tagLibraryInfo;

		// ibr_12109
		// http://lightbody.net/blog/2005/07/hashmapget_can_cause_an_infini.html
		this.handlerClassCache = new ConcurrentHashMap<String, Class<?>>(1000);

	}

	public void clear() {
		this.handlerClassCache.clear();
	}

	public void destroy() {
		this.handlerClassCache = null;
	}

	/**
	 * handler name�� �ش��ϴ� Tag handler�� �����Ѵ�. handlerName�� �ش��ϴ� handler�� ã�� ���� ��� library�� default tag handler�� �����Ѵ�.
	 * 
	 * @param handlerName
	 * @return null if the tag handler according to tag name does not exist.
	 */
	public Tag getTagHandler(String handlerName) {
		Tag handler = null;

		// �׳� �����ϴ°� �� ������ ��.��
		try {
			Class<?> clazz = (Class<?>) this.handlerClassCache.get(handlerName);

			if (clazz == null) {
				TagInfo tagInfo = this.tagLibraryInfo.getTag(handlerName);

				if (tagInfo == null) {
					// �ױ� �̸��� �´� �ڵ鷯�� ã�� ���Ͽ��� ���
					// library�� default tag handler�� ����Ѵ� -> �Ϲ������� custom tag �� ó���ϰ� �ȴ�.

					tagInfo = this.tagLibraryInfo.getTag(this.tagLibraryInfo.getDefaultTagName());
				}

				clazz = Class.forName(tagInfo.getTagClassName());
				this.handlerClassCache.put(handlerName, clazz);
			}

			handler = (Tag) clazz.newInstance();
		} catch (Exception ex) {
			handler = null;
		}
		return handler;
	}

	/**
	 * host���� ������ tag handler�� ��ȯ�Ѵ�.
	 * 
	 * @param handlerName
	 * @param handler
	 */
	public void releaseTagHandler(String handlerName, Tag handler) {

	}

}
