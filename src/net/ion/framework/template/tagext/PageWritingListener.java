package net.ion.framework.template.tagext;

/**
 * page 쓰기 이벤트를 수신한다. 이 listener는 pageWriter에 등록할 수 있다.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */
public interface PageWritingListener {
	void write(String s);
}
