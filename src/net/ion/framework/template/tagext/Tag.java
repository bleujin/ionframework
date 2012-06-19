package net.ion.framework.template.tagext;

/**
 * tag의 기본 모델
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public interface Tag {
	public static final int SKIP_PAGE = 5;
	public static final int EVAL_PAGE = 6;

	/**
	 * template 내 tag 사이 계층 관계를 구성한다.
	 * 
	 * @param tag
	 *            Tag
	 */
	public abstract void setParent(Tag tag);

	public abstract Tag getParent();

	/**
	 * 사용한 tag를 정리할 때 불러진다.
	 */
	public abstract void release();
}
