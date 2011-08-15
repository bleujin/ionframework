package net.ion.framework.template.tagext;

import java.util.Enumeration;
import java.util.Hashtable;

import net.ion.framework.template.parse.Marker;

/**
 * Tag �⺻ ���� Ŭ����, �� Ŭ������ ����Ͽ� �ױ׸� �ۼ��Ѵ�. TagSupport,PageTagSupport�� ���밡 �ȴ�.
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
	 * tag�� release�� �� �ҷ�����.
	 */
	public void release() {
		values = null;
		parent = null;
		id = null;
		tagMark = null;
	}

	/**
	 * �θ� tag�� �����Ѵ�.
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
	 * TemplateRuntime�� ���� �ҷ�����.
	 * 
	 * @param t
	 */
	public void setParentRuntimeTag(Tag t) {
		parentRuntimeTag = t;
	}

	/**
	 * Template Runtime�� ���������� ��ɶ� ���� Runtime�� ǰ�� �ִ� �θ� tag�� �˰��� �� �� �θ���.
	 * 
	 * @return
	 */
	public Tag getParentRuntimeTag() {
		return parentRuntimeTag;
	}

	/**
	 * �ױ� �̸��� �����Ѵ�. [[--AAAStart,attr1:value1--]]...body...[[--AAAEnd--]] �� ��� AAA�� �����Ѵ�.
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
	 * template�������� ���� �ױ��� ��ġ�� �����Ѵ�.
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
	 * �ױ׺� �Ӽ� ���� �����Ѵ�.
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
	 * klass Ÿ���� �θ� tag�� from ���� ã�´�.
	 * 
	 * @param from
	 *            Tag ã�� ������ tag
	 * @param klass
	 *            Class ã�� class type
	 * @return Tag ã�� ���ϸ� null
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
