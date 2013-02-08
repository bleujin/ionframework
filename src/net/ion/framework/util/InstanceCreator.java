package net.ion.framework.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;

import net.ion.framework.configuration.Configuration;
import net.ion.framework.configuration.ConfigurationException;
import net.ion.framework.configuration.NotFoundXmlTagException;

/**
 * Java Introspection & Reflection 기능을 이용할 때 Class 와 Object를 얻기 쉽게한다. String으로 된 Class 이름과 Value를 주면 해당 Class 또는 Object로 Instance화 시킨다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class InstanceCreator {
	// "동적으로 object를 사용하는 곳에서 자주 사용하게 될 것이얌 ㅡ.ㅡV"

	// 이 클래스는 생성 못해요 -,.-
	// 전부 static class
	private InstanceCreator() {
	}

	/**
	 * configuraion에 작성된 설정대로 object를 생성한다.
	 * 
	 * <pre>
	 * * configuration 작성 예제:
	 * 
	 *   &lt;configured-object&gt;
	 *       &lt;class-name&gt;java.util.logging.FileHandler&lt;/class-name&gt;
	 *       &lt;constructor&gt;
	 *           &lt;constructor-param&gt;
	 *               &lt;description&gt;pattern&lt;/description&gt;
	 *               &lt;type&gt;java.lang.String&lt;/type&gt;
	 *               &lt;value&gt;%h/java%u.log&lt;/value&gt;
	 *           &lt;/constructor-param&gt;
	 *           &lt;constructor-param&gt;
	 *               &lt;description&gt;limit&lt;/description&gt;
	 *               &lt;type&gt;int&lt;/type&gt;
	 *               &lt;value&gt;50000&lt;/value&gt;
	 *           &lt;/constructor-param&gt;
	 *           &lt;constructor-param&gt;
	 *               &lt;description&gt;count&lt;/description&gt;
	 *               &lt;type&gt;int&lt;/type&gt;
	 *               &lt;value&gt;1&lt;/value&gt;
	 *           &lt;/constructor-param&gt;
	 *           &lt;constructor-param&gt;
	 *               &lt;description&gt;append&lt;/description&gt;
	 *               &lt;type&gt;boolean&lt;/type&gt;
	 *               &lt;value&gt;true&lt;/value&gt;
	 *           &lt;/constructor-param&gt;
	 *       &lt;/constructor&gt;
	 *       &lt;property&gt;
	 *           &lt;name&gt;name&lt;/name&gt;
	 *           &lt;value&gt;fileHandler&lt;/value&gt;
	 *       &lt;/property&gt;
	 *   &lt;/configured-object&gt;
	 * </pre>
	 * 
	 * &lt;class-name/&gt; 는 반드시 필요하며 &lt;constructor/&gt; 와 &lt;property/&gt; 는 없어도 된다. <br>
	 * <br>
	 * * inner configured object를 허용한다. -> <b> &lt;value/&gt; 안에 &lt;configured-object/&gt; 가 들어가도 괜찮다.!!</b> <br>
	 * &lt;value/&gt; 로 null을 넣고 싶을 경우 <b>&lt;null/&gt;</b> 를 입력한다.
	 * 
	 * <pre>
	 * ex)
	 *      ...
	 *      &lt;value&gt;
	 *          &lt;configured-object&gt;
	 *              &lt;class-name&gt;java.util.logging.ConsoleHandler&lt;/class-name&gt;
	 *          &lt;/configured-object&gt;
	 *      &lt;/value&gt;
	 *      ...
	 * </pre>
	 * 
	 * @param config
	 * @return
	 * @throws InstanceCreationException
	 */
	public static Object createConfiguredInstance(Configuration config) throws InstanceCreationException {
		/*
		 * <configured-object> <class-name>java.util.logging.FileHandler</class-name> <constructor> <constructor-param> <description>pattern</description> <type>java.lang.String</type> <value>%h/java%u.log</value> </constructor-param>
		 * <constructor-param> <description>limit</description> <type>int</type> <value>50000</value> </constructor-param> <constructor-param> <description>count</description> <type>int</type> <value>1</value> </constructor-param> <constructor-param>
		 * <description>append</description> <type>boolean</type> <value>true</value> </constructor-param> </constructor> <property> <name></name> <value></value> </property> </configured-object>
		 */
		if (!config.getTagName().equals("configured-object")) {
			throw new InstanceCreationException("invalid configuration. only <configured-object/> allowed.");
		}

		try {
			// 생성자 파라미터가 존재할 때
			Object object = null;
			String className = config.getChild("class-name").getValue();
			// if (className.equals("null"))
			// return null;

			Class<?> objectClass = Class.forName(className);

			// construct
			try {
				Configuration[] constructorParams = config.getChildren("constructor.constructor-param");
				Class<?>[] paramClasses = new Class[constructorParams.length];
				Object[] paramObjects = new Object[constructorParams.length];

				for (int j = 0; j < constructorParams.length; ++j) {
					String paramType = constructorParams[j].getChild("type").getValue();
					Class<?> paramClass = getClassInstance(paramType);
					Object paramObject = null;

					try {
						// value가 다시 <configured-object/> 로 구성된 object 일 경우 재귀 호출!!
						paramObject = InstanceCreator.createConfiguredInstance(constructorParams[j].getChild("value.configured-object"));
					} catch (NotFoundXmlTagException nx) {
						try {
							// value가 다시 <null/> 일 경우 null
							if (constructorParams[j].getChild("value.null") != null)
								paramObject = null;
						} catch (NotFoundXmlTagException nx2) {
							// value가 <null/> 가 아닐 경우
							String paramValue = constructorParams[j].getChild("value").getValue();
							paramObject = getObjectInstance(paramClass, paramValue);
						}
					}

					paramClasses[j] = paramClass;
					paramObjects[j] = paramObject;
				}

				object = objectClass.getConstructor(paramClasses).newInstance(paramObjects);
			} catch (NotFoundXmlTagException notFoundEx) {
				// 생성자의 파라미터가 존재 하지 않을 때
				object = objectClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// property
			try {
				Configuration[] properties = config.getChildren("property");

				// property 의 set method 를 table화 시킴
				BeanInfo beanInfo = Introspector.getBeanInfo(objectClass);
				PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
				HashMap<String, Method> methodMap = new HashMap<String, Method>();
				for (int i = 0; i < descriptors.length; ++i) {
					String propertyName = descriptors[i].getName(); // .toLowerCase(); 주의!!!!!!
					Method method = descriptors[i].getWriteMethod();

					methodMap.put(propertyName, method);
				}

				for (int i = 0; i < properties.length; ++i) {
					Configuration property = properties[i];
					String name = property.getChild("name").getValue(); // .toLowerCase(); 주의!!!!!!!

					Method setter = (Method) methodMap.get(name);
					if (setter == null) {
						throw new InstanceCreationException("could not find setter method of property name:" + name);
					}

					if (setter.getParameterTypes().length != 1) {
						throw new InstanceCreationException("unknown parameter for property setter:propery name=" + name);
					}

					Object paramObject = null;
					try {
						// value가 다시 <configured-object/> 로 구성된 object 일 경우 재귀 호출!!
						paramObject = InstanceCreator.createConfiguredInstance(property.getChild("value.configured-object"));
					} catch (NotFoundXmlTagException nx) {
						try {
							// value가 다시 <null/> 일 경우 null
							if (property.getChild("value.null") != null)
								paramObject = null;
						} catch (NotFoundXmlTagException nx2) {
							// value가 <null/> 가 아닐 경우
							String value = property.getChild("value").getValue();
							paramObject = getObjectInstance(setter.getParameterTypes()[0], value);
						}
					}

					setter.invoke(object, new Object[] { paramObject });
				}
			} catch (NotFoundXmlTagException nfxte) {
				// skip setting properties.
				// System.out.println("InstanceCreator:Skipped setting properties.");
			}
			// debug
			// System.out.println(object);

			return object;

		} catch (ConfigurationException cx) {
			throw new InstanceCreationException("Occurred configuration exception.", cx);
		} catch (ClassNotFoundException cnfe) {
			throw new InstanceCreationException("Class not found.", cnfe);
		} catch (Exception e) {
			throw new InstanceCreationException("Could not instantiate the object.", e);
		}
	}

	/**
	 * className 에 해당하는 Class Instance를 리턴 (SystemClassLoader를 사용한다.)
	 * 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClassInstance(String className) throws ClassNotFoundException {
		return getClassInstance(className, ClassLoader.getSystemClassLoader());
	}

	private static HashMap<String, Class<?>> classCache = new HashMap<String, Class<?>>();

	/**
	 * className 에 해당하는 class를 가져온다.
	 * 
	 * primitive type 일 경우에는 해당하는 class type를 가져온다.
	 * 
	 * @param loader
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClassInstance(String className, ClassLoader loader) throws ClassNotFoundException {
		Class<?> cls = classCache.get(className);
		if (cls == null) {
			try {
				cls = loader.loadClass(className);
			} catch (ClassNotFoundException ex) {
				// primitive type인지 체크
				if (className.equals("int")) {
					cls = int.class;
				} else if (className.equals("boolean")) {
					cls = boolean.class;
				} else if (className.equals("byte")) {
					cls = byte.class;
				} else if (className.equals("char")) {
					cls = char.class;
				} else if (className.equals("short")) {
					cls = short.class;
				} else if (className.equals("long")) {
					cls = long.class;
				} else if (className.equals("float")) {
					cls = float.class;
				} else if (className.equals("double")) {
					cls = double.class;
				} else {
					throw ex;
				}
			}

			classCache.put(className, cls);
			return cls;
		} else {
			return cls;
		}
	}

	/**
	 * clazz를 value로 생성한 새로운 object를 리턴 clazz가 primitive type일 경우 해당하는 wapper class를 생성해서 돌려준다.
	 * 
	 * clazz 의 생성자의 인자가 반드시 String 형이 있어야 생성 가능하다.
	 * 
	 * <pre>
	 * clazz 가 primitive type일 경우 그 type에 해당하는 wapper class를 object화 하여 리턴한다.
	 * 자바 reflection 기능을 사용할 때 유용하다.
	 * 
	 * ex) getObject(int.class,"10") -> Interger.valueOf(10) 과 동일
	 * 
	 * </pre>
	 * 
	 * @param clazz
	 * @param value
	 * @return 생성할 수 없을 경우 null
	 * 
	 * @see net.ion.framework.logging.LogBroker
	 */
	public static Object getObjectInstance(Class<?> clazz, String value) throws InstanceCreationException {
		Object object = null;

		if (clazz.isPrimitive()) {
			if (clazz == int.class) {
				try {
					object = Integer.valueOf(value);
				} catch (NumberFormatException nf) { // value가 숫자형이 아니어서 에러가 생기면 0으로
					object = new Integer(0);
				}
			} else if (clazz == boolean.class) {
				// 원래 spec에 따르면 "yes"는 boolean으로 false이지만 "yes"일 경우에도 true로 한다. 그외에도 T,Y도 true로 한다.
				if (value.equalsIgnoreCase("T") || value.equalsIgnoreCase("YES") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("Y")) {
					object = Boolean.TRUE;
				} else {
					object = Boolean.valueOf(value);
				}
			} else if (clazz == byte.class) {
				object = Byte.valueOf(value);
			} else if (clazz == char.class) {
				object = new Character(value.charAt(0));
			} else if (clazz == short.class) {
				object = Short.valueOf(value);
			} else if (clazz == long.class) {
				object = Long.valueOf(value);
			} else if (clazz == float.class) {
				object = Float.valueOf(value);
			} else if (clazz == double.class) {
				object = Double.valueOf(value);
			}
		} else {
			if (clazz == String.class) {
				object = value;
			} else if (clazz == Boolean.class) {
				// 원래 spec에 따르면 "yes"는 boolean으로 false이지만 "yes"일 경우에도 true로 한다. 그외에도 T,Y도 true로 한다.
				if (value.equalsIgnoreCase("T") || value.equalsIgnoreCase("YES") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("Y")) {
					object = Boolean.TRUE;
				} else {
					object = Boolean.valueOf(value);
				}
			} else {
				try {
					object = clazz.getConstructor(new Class[] { String.class }).newInstance(new Object[] { value });
				} catch (Exception e) {
					throw new InstanceCreationException(e);
				}
			}
		}

		return object;
	}

	/**
	 * @param className
	 *            생성하고자 하는 className ex) java.lang.Integer , int
	 * @param value
	 *            생성시에 사용할 value ex) 10
	 * @return
	 */
	public static Object getObjectInstance(String className, String value) throws ClassNotFoundException, InstanceCreationException {
		Class<?> cls = getClassInstance(className);
		return getObjectInstance(cls, value);
	}

	/**
	 * class의 property write method를 method name을 key로 하여 hash map으로 만든다.
	 * 
	 * @param clazz
	 * @return
	 * @throws InstanceCreationException
	 */
	private static HashMap<String, Method> getWriteMethodMapTable(Class<?> clazz) throws InstanceCreationException {
		try {
			// property 의 set method 를 table화 시킴
			HashMap<String, Method> methodMap = cachedMethodMapTable.get(clazz);

			if (methodMap == null) {
				BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
				PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
				methodMap = new HashMap<String, Method>();

				for (int i = 0; i < descriptors.length; ++i) {
					String propertyName = descriptors[i].getName(); // .toLowerCase(); !!!!!!!!!!!! 주의!!!!
					Method method = descriptors[i].getWriteMethod();

					methodMap.put(propertyName, method);
				}

				// 다음번 삽질을 피하기 위해 caching한다~
				cachedMethodMapTable.put(clazz, methodMap);
			}

			return methodMap;
		} catch (Throwable t) {
			throw new InstanceCreationException(t);
		}
	}

	private static Hashtable<Class<?>, HashMap<String, Method>> cachedMethodMapTable = new Hashtable<Class<?>, HashMap<String, Method>>(); // write method 를 cache해서 introspection을 덜 하도록..

	/**
	 * property name의 value를 설정한 bean object를 생성한다.
	 * 
	 * <p>
	 * 주의: 같은 property에 해당하는 names와 values는 index가 서로 일치해야한다.
	 * </p>
	 * 
	 * @param beanClass
	 * @param propertyNames
	 * @param propertyValues
	 * @return
	 * @throws InstanceCreationException
	 */
	public static Object createBeanWithPropertyValues(Class<?> beanClass, String[] propertyNames, Object[] propertyValues) throws InstanceCreationException {
		try {
			Object object = beanClass.newInstance();
			return setPropertyValues(object, propertyNames, propertyValues);
		} catch (IllegalAccessException ex) {
			throw new InstanceCreationException(ex);
		} catch (InstantiationException ex) {
			throw new InstanceCreationException(ex);
		}
	}

	/**
	 * bean object의 property를 해당하는 key value로 setting 한다.
	 * 
	 * <p>
	 * 주의: 같은 property에 해당하는 names와 values는 index가 서로 일치해야한다.
	 * </p>
	 * 
	 * @param beanObject
	 * @param propertyNames
	 * @param propertyValues
	 * @return
	 * @throws InstanceCreationException
	 */
	public static Object setPropertyValues(Object beanObject, String[] propertyNames, Object[] propertyValues) throws InstanceCreationException {
		try {
			// property 의 set method 를 table화 시킴
			Class<?> beanClass = beanObject.getClass();
			HashMap<String, Method> methodMap = getWriteMethodMapTable(beanClass);

			String[] names = propertyNames;
			for (int i = 0; i < names.length; ++i) {
				String name = names[i]; // .toLowerCase(); 주의!!!!!!!!!!!!!!
				Object value = propertyValues[i];
				Method setter = (Method) methodMap.get(name);

				if (setter == null) {
					System.out.println("could not find setter method of property name:" + name);
					continue;
				}

				try {
					setter.invoke(beanObject, new Object[] { value });
				} catch (Exception ex) { // 여기 부분을 최적화 할 필요가 있음.
					if (value == null) { // primitive 타입에 null을 집어 넣었을 경우 여기로 온다. (object형에는 null을 줄 수 있으니 exception이 발생하지 않는다.)
						System.err.println("InstanceCreator.setPropertyValues():CAUTION:primitive type do not allow null.:setting passed.");
					} else { // setter의 parameter 타입이 맞지 않을 경우 - type mismatch
						Class<?> param = setter.getParameterTypes()[0];
						if (param.isPrimitive()) {
							// setter의 argument가 primitive형이면서 value가 Number형이면 형을 맞게 변형시켜 재시도!
							// 부모가 Number형이면 type을 찾아서 재시도!
							value = getObjectInstance(param, value.toString());
							setter.invoke(beanObject, new Object[] { value });
						} else {
							throw ex;
						}
					}
				}

			}

			return beanObject;
		} catch (Throwable t) {
			throw new InstanceCreationException(t);
		}
	}

	// /**
	// * method parameter type와 object의 type을 체크한 후 object 형을 변형해야할 경우 변형하여 리턴한다.
	// * 이외의 경우 input된 o를 그대로 리턴한다.
	// * <pre>
	// * 1. method parameter type이 primitive일 경우
	// * a. object type이 Number형의 자손일때 적절한 primitive wrapper object로 변환하여 리턴
	// * b. object 가 null일 경우 exception 발생
	// * </pre>
	// *
	// * @param m
	// * @param o
	// * @return
	// * @throws InstanceCreationException
	// */
	// private static Object adjustMethodParameterType(Method m,Object o) throws InstanceCreationException
	// {
	// Class param = m.getParameterTypes()[0];
	//
	// if (param.isPrimitive())
	// {
	// if ( o == null ) throw new InstanceCreationException("primitive type do not allow null.");
	//
	// if ( o.getClass().getSuperclass() == Number.class )
	// {
	// o = getObjectInstance(param,o.toString());
	// }
	// }
	//
	// return o;
	// }

}
