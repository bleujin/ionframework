package net.ion.framework.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * http request�� ������ ���ڼ����� ���ڵ��ϴ� servlet filter<br/>
 * <br/>
 * ������ ������ ���� ��� servlet request�� character set�� null�� ��� ISO-8859-1�� default encoding �ȴ�. �� Ŭ������ request�� character set�� null�� ��� request�� locale�� ���� �ش� locale�� default charset���� �⺻ ���ڵ��� �ǵ��� �Ѵ�.
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
