package net.ion.framework.template.tagext;

import java.util.Enumeration;
import java.util.Hashtable;

import net.ion.framework.template.parse.Marker;

/**
 * Tag 기본 구현 클래스, 이 클래스를 상속하여 테그를 작성한다. TagSupport,PageTagSupport의 뼈대가 된다.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 * @see net.ion.framework.template.Tag
 */
public class BaseTagSupport implements Tag {
	private Tag parent = null;
	private Hashtable<String, Object> values = null;

	protected String id = null;
	protected Marker tagMark = null;

	/**
	 * tag가 release될 때 불러진다.
	 */
	public void release() {
		values = null;
		parent = null;
		id = null;
		tagMark = null;
	}

	/**
	 * 부모 tag를 지정한다.
	 * 
	 * @param t
	 *            Tag
	 */
	public void setParent(Tag t) {
		parent = t;
	}

	public Tag getParent() {
		return parent;
	}

	private Tag parentRuntimeTag = null;

	/**
	 * TemplateRuntime에 의해 불러진다.
	 * 
	 * @param t
	 */
	public void setParentRuntimeTag(Tag t) {
		parentRuntimeTag = t;
	}

	/**
	 * Template Runtime이 계층적으로 운영될때 하위 Runtime을 품고 있는 부모 tag를 알고자 할 때 부른다.
	 * 
	 * @return
	 */
	public Tag getParentRuntimeTag() {
		return parentRuntimeTag;
	}

	/**
	 * 테그 이름을 지정한다. [[--AAAStart,attr1:value1--]]...body...[[--AAAEnd--]] 일 경우 AAA를 리턴한다.
	 * 
	 * @param id
	 *            String
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	/**
	 * template본문에서 현재 테그의 위치를 지정한다.
	 * 
	 * @param mark
	 *            Marker
	 */
	public void setTagMarker(Marker mark) {
		this.tagMark = mark;
	}

	public Marker getTagMarker() {
		return this.tagMark;
	}

	/**
	 * 테그별 속성 값을 지정한다.
	 * 
	 * @param k
	 *            String
	 * @param o
	 *            Object
	 */
	public void setValue(String k, Object o) {
		if (values == null) {
			values = new Hashtable<String, Object>();
		}
		values.put(k, o);
	}

	public Object getValue(String k) {
		if (values == null) {
			return null;
		} else {
			return values.get(k);
		}
	}

	public void removeValue(String k) {
		if (values != null) {
			values.remove(k);
		}
	}

	public Enumeration<String> getValues() {
		if (values == null) {
			return null;
		} else {
			return values.keys();
		}
	}

	/**
	 * klass 타입의 부모 tag를 from 부터 찾는다.
	 * 
	 * @param from
	 *            Tag 찾기 시작할 tag
	 * @param klass
	 *            Class 찾는 class type
	 * @return Tag 찾지 못하면 null
	 */
	public static final Tag findAncestorWithClass(Tag from, Class<?> klass) {
		boolean isInterface = false;
		if (from == null || klass == null || !(net.ion.framework.template.tagext.Tag.class).isAssignableFrom(klass) && !(isInterface = klass.isInterface())) {
			return null;
		}

		do {
			Tag tag = from.getParent();
			if (tag == null) {
				return null;
			}
			if (isInterface && klass.isInstance(tag) || klass.isAssignableFrom(tag.getClass())) {
				return tag;
			}
			from = tag;
		} while (true);
	}
}
