package net.ion.framework.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * http request를 적절한 문자셋으로 인코딩하는 servlet filter<br/>
 * <br/>
 * 별도의 설정이 없을 경우 servlet request의 character set이 null일 경우 ISO-8859-1로 default encoding 된다. 이 클래스는 request의 character set이 null일 경우 request의 locale을 보고 해당 locale의 default charset으로 기본 인코딩이 되도록 한다.
 * 
 * <pre>
 *  ko -> EUC-KR
 *  ja -> sjis
 *  en -> ISO-8859-1
 *  ...
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public class CharacterEncodingFilter implements Filter {
	public CharacterEncodingFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request.getCharacterEncoding() == null) {
			request.setCharacterEncoding(CharsetMapper.getDefaultCharset(request.getLocale()));
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}
