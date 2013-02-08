package net.ion.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.UnsupportedEncodingException;

/**
 * Object를 String으로 변환해 준다 거꾸로 변환된 String을 Object로 되돌린다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public class SerializedString {

	private SerializedString() {
	}

	/**
	 * object를 직렬화 한다.
	 * 
	 * @param o
	 *            Object
	 * @throws SerializedStringException
	 * @return String
	 */
	public static String serialize(Object o) throws SerializedStringException {
		return serialize(o, false);
	}

	/**
	 * object를 직렬화 한다.
	 * 
	 * @param o
	 *            Object
	 * @param warp
	 *            boolean true이면 80글자 단위로 자른다.
	 * @throws SerializedStringException
	 * @return String
	 */
	public static String serialize(Object o, boolean warp) throws SerializedStringException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.flush();

			String r = new String(baos.toByteArray(), "8859_1");
			String s = encodeString(r, warp);

			return s;
		} catch (Exception e) {
			throw new SerializedStringException(e.getMessage(), e);
		}
	}

	/**
	 * 문자열을 object로 역직렬화 한다.
	 * 
	 * @param s
	 *            String
	 * @throws SerializedStringException
	 * @return Object
	 */
	public static Object deserialize(String s) throws SerializedStringException {
		return deserialize(s, false);
	}

	/**
	 * 문자열을 object로 역직렬화 한다.
	 * 
	 * @param s
	 *            String
	 * @param warp
	 *            boolean
	 * @throws SerializedStringException
	 * @return Object
	 */
	public static Object deserialize(String s, boolean warp) throws SerializedStringException {
		try {
			String r = decodeString(s, warp);
			byte[] b = r.getBytes("8859_1");

			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();

			return o;
		} catch (Exception e) {
			throw new SerializedStringException(e.getMessage(), e);
		}
	}

	/**
	 * @param cls
	 *            Class 역직렬화 할 클래스
	 * @param s
	 *            String
	 * @param warp
	 *            boolean
	 * @throws SerializedStringException
	 * @return Object
	 */
	public static Object deserialize(final Class<?> cls, String s, boolean warp) throws SerializedStringException {
		try {
			String r = decodeString(s, warp);
			byte[] b = r.getBytes("8859_1");

			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bais) {
				protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
					if (osc.toString().equals(ObjectStreamClass.lookup(cls).toString()))
						return cls;
					else
						return super.resolveClass(osc);
				}
			};
			Object o = ois.readObject();

			return o;
		} catch (Exception e) {
			throw new SerializedStringException(e.getMessage(), e);
		}
	}

	private static String decodeString(String s, boolean warp) {
		try {
			if (warp)
				s = warpBase64Line(s, false);
			char[] ca = s.toCharArray();
			for (int i = 0, length = ca.length; i < length; ++i) {
				switch (ca[i]) {
				case '@':
					ca[i] = '=';
					break;
				case '*':
					ca[i] = '+';
					break;
				case '|':
					ca[i] = '/';
					break;
				default:
				}
			}
			return new String(Base64.base64ToByteArray(new String(ca)), "8859_1");
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	private static String encodeString(String r, boolean warp) {
		try {
			String ss = Base64.byteArrayToBase64(r.getBytes("8859_1"));
			char[] ca = ss.toCharArray();
			for (int i = 0, length = ca.length; i < length; ++i) {
				switch (ca[i]) {
				case '=':
					ca[i] = '@';
					break;
				case '+':
					ca[i] = '*';
					break;
				case '/':
					ca[i] = '|';
					break;
				default:
				}
			}
			String rt = new String(ca);
			if (warp)
				return warpBase64Line(rt, true);
			else
				return rt;
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	/**
	 * @param s
	 *            String
	 * @param warp
	 *            boolean true if split , false if join
	 * @return String
	 */
	private static String warpBase64Line(String s, boolean split) {
		if (split) {
			StringBuffer buf = new StringBuffer();
			final int LIMIT = 80;
			int cnt = 0;
			for (int i = 0, length = s.length(); i < length; ++i) {
				buf.append(s.charAt(i));
				if (++cnt == LIMIT) {
					buf.append('\n');
					cnt = 0;
				}
			}
			return buf.toString();
		} else {
			// ' ' '\n' 을 다 없애 버린다.
			StringBuffer buf = new StringBuffer();
			for (int i = 0, length = s.length(); i < length; ++i) {
				char c = s.charAt(i);
				if (c == ' ' || c == '\n') {
					;
				} else {
					buf.append(c);
				}
			}
			return buf.toString();
		}
	}

	/**
	 * class 객체를 직렬화 한다.
	 * 
	 * @param name
	 *            String 직렬화 하고자 하는 class의 fully qualified class name
	 * @param cl
	 *            ClassLoader null if uses system default class loader
	 * @return String null if exception
	 */
	public static final String saveResource(String name, ClassLoader cl) {
		try {
			ClassLoader loader = ((cl == null) ? ClassLoader.getSystemClassLoader() : cl );
			String classPath = StringUtil.join(StringUtil.split(name, "."), "/");
			if(!classPath.endsWith(".class")){
				classPath += ".class";
			}

			InputStream is = loader.getResourceAsStream(classPath);
			if (is == null)
				return null;

			byte[] code = StreamUtils.toByteArray(is);
			String codeString = Base64.byteArrayToBase64(code);
			return warpBase64Line(codeString, true);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 직렬화 된 class string 을 name으로 loading하는 classloader를 생성한다.
	 * 
	 * @param name
	 *            String fully qualified class name
	 * @param classString
	 *            String serialized class string
	 * @param parentClassLoader
	 *            ClassLoader
	 * @return ClassLoader
	 */
	public static final ClassLoader createClassLoader(final String name, final String classString, final ClassLoader parentClassLoader) {
		ClassLoader loader = new ClassLoader(parentClassLoader) {
			protected Class<?> findClass(String findName) throws ClassNotFoundException {
				if (findName.equals(name)) {
					byte[] b = null;
					String codeString = warpBase64Line(classString, false);
					b = Base64.base64ToByteArray(codeString);
					try {
						Class<?> cls = defineClass(name, b, 0, b.length);
						// resolveClass(cls);
						return cls;
					} catch (Throwable ex) {
						ex.printStackTrace();
						return null;
					}
				} else
					return getParent().loadClass(findName);
			}
		};

		return loader;
	}

	/**
	 * 직렬화된 class string을 name으로 loading 하여 class 객체를 리턴한다.
	 * 
	 * @param name
	 *            String
	 * @param classString
	 *            String
	 * @return Class null if class not found
	 */
	public static final Class<?> loadClass(final String name, final String classString, final ClassLoader parentClassLoader) {
		ClassLoader loader = createClassLoader(name, classString, parentClassLoader);

		try {
			return Class.forName(name, true, loader);
			// return loader.loadClass(name);
		} catch (ClassNotFoundException ex) {
			return null;
		}
	}

	/**
	 * 직렬화된 class string을 name으로 loading 하여 class 객체를 리턴한다.
	 * 
	 * use Thread.currentThread().getContextClassLoader() as a parent class loader
	 * 
	 * @param name
	 *            String fully qualified class name
	 * @param classString
	 *            String
	 * @return Class 실패시 null
	 */
	public static final Class<?> loadClass(final String name, final String classString) {
		return loadClass(name, classString, Thread.currentThread().getContextClassLoader());
	}

	// public static void main(String[] args)
	// {
	// String s = saveResource("net/ion/framework/util/SerializedString.class",null);
	// // encoded class
	// System.out.println(s);
	//
	// Class cls =loadClass("net.ion.framework.util.SerializedString",s);
	// System.out.println(cls);
	// }
}
