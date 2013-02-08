package net.ion.framework.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ion.framework.template.tagext.Tag;
import net.ion.framework.template.tagext.TagInfo;
import net.ion.framework.template.tagext.TagLibraryInfo;

/**
 * tagName에 해당하는 tag handling object 를 리턴한다.
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
	 *            각 handler 마다 최대 크기
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
	 * handler name에 해당하는 Tag handler를 리턴한다. handlerName에 해당하는 handler를 찾지 못할 경우 library의 default tag handler를 리턴한다.
	 * 
	 * @param handlerName
	 * @return null if the tag handler according to tag name does not exist.
	 */
	public Tag getTagHandler(String handlerName) {
		Tag handler = null;

		// 그냥 생성하는게 더 빠르네 ㅡ.ㅡ
		try {
			Class<?> clazz = (Class<?>) this.handlerClassCache.get(handlerName);

			if (clazz == null) {
				TagInfo tagInfo = this.tagLibraryInfo.getTag(handlerName);

				if (tagInfo == null) {
					// 테그 이름에 맞는 핸들러를 찾지 못하였을 경우
					// library의 default tag handler를 사용한다 -> 일반적으로 custom tag 를 처리하게 된다.

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
	 * host에서 가져간 tag handler를 반환한다.
	 * 
	 * @param handlerName
	 * @param handler
	 */
	public void releaseTagHandler(String handlerName, Tag handler) {

	}

}
