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
 * Java Introspection & Reflection ����� �̿��� �� Class �� Object�� ��� �����Ѵ�. String���� �� Class �̸��� Value�� �ָ� �ش� Class �Ǵ� Object�� Instanceȭ ��Ų��.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class InstanceCreator {
	// "�������� object�� ����ϴ� ������ ���� ����ϰ� �� ���̾� ��.��V"

	// �� Ŭ������ ���� ���ؿ� -,.-
	// ���� static class
	private InstanceCreator() {
	}

	/**
	 * configuraion�� �ۼ��� ������� object�� �����Ѵ�.
	 * 
	 * <pre>
	 * * configuration �ۼ� ����:
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
	 * &lt;class-name/&gt; �� �ݵ�� �ʿ��ϸ� &lt;constructor/&gt; �� &lt;property/&gt; �� ��� �ȴ�. <br>
	 * <br>
	 * * inner configured object�� ����Ѵ�. -> <b> &lt;value/&gt; �ȿ� &lt;configured-object/&gt; �� ���� ������.!!</b> <br>
	 * &lt;value/&gt; �� null�� �ְ� ���� ��� <b>&lt;null/&gt;</b> �� �Է��Ѵ�.
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
			// ������ �Ķ���Ͱ� ������ ��
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
						// value�� �ٽ� <configured-object/> �� ������ object �� ��� ��� ȣ��!!
						paramObject = InstanceCreator.createConfiguredInstance(constructorParams[j].getChild("value.configured-object"));
					} catch (NotFoundXmlTagException nx) {
						try {
							// value�� �ٽ� <null/> �� ��� null
							if (constructorParams[j].getChild("value.null") != null)
								paramObject = null;
						} catch (NotFoundXmlTagException nx2) {
							// value�� <null/> �� �ƴ� ���
							String paramValue = constructorParams[j].getChild("value").getValue();
							paramObject = getObjectInstance(paramClass, paramValue);
						}
					}

					paramClasses[j] = paramClass;
					paramObjects[j] = paramObject;
				}

				object = objectClass.getConstructor(paramClasses).newInstance(paramObjects);
			} catch (NotFoundXmlTagException notFoundEx) {
				// �������� �Ķ���Ͱ� ���� ���� ���� ��
				object = objectClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// property
			try {
				Configuration[] properties = config.getChildren("property");

				// property �� set method �� tableȭ ��Ŵ
				BeanInfo beanInfo = Introspector.getBeanInfo(objectClass);
				PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
				HashMap<String, Method> methodMap = new HashMap<String, Method>();
				for (int i = 0; i < descriptors.length; ++i) {
					String propertyName = descriptors[i].getName(); // .toLowerCase(); ����!!!!!!
					Method method = descriptors[i].getWriteMethod();

					methodMap.put(propertyName, method);
				}

				for (int i = 0; i < properties.length; ++i) {
					Configuration property = properties[i];
					String name = property.getChild("name").getValue(); // .toLowerCase(); ����!!!!!!!

					Method setter = (Method) methodMap.get(name);
					if (setter == null) {
						throw new InstanceCreationException("could not find setter method of property name:" + name);
					}

					if (setter.getParameterTypes().length != 1) {
						throw new InstanceCreationException("unknown parameter for property setter:propery name=" + name);
					}

					Object paramObject = null;
					try {
						// value�� �ٽ� <configured-object/> �� ������ object �� ��� ��� ȣ��!!
						paramObject = InstanceCreator.createConfiguredInstance(property.getChild("value.configured-object"));
					} catch (NotFoundXmlTagException nx) {
						try {
							// value�� �ٽ� <null/> �� ��� null
							if (property.getChild("value.null") != null)
								paramObject = null;
						} catch (NotFoundXmlTagException nx2) {
							// value�� <null/> �� �ƴ� ���
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
	 * className �� �ش��ϴ� Class Instance�� ���� (SystemClassLoader�� ����Ѵ�.)
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
	 * className �� �ش��ϴ� class�� �����´�.
	 * 
	 * primitive type �� ��쿡�� �ش��ϴ� class type�� �����´�.
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
				// primitive type���� üũ
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
	 * clazz�� value�� ������ ���ο� object�� ���� clazz�� primitive type�� ��� �ش��ϴ� wapper class�� �����ؼ� �����ش�.
	 * 
	 * clazz �� �������� ���ڰ� �ݵ�� String ���� �־�� ���� �����ϴ�.
	 * 
	 * <pre>
	 * clazz �� primitive type�� ��� �� type�� �ش��ϴ� wapper class�� objectȭ �Ͽ� �����Ѵ�.
	 * �ڹ� reflection ����� ����� �� �����ϴ�.
	 * 
	 * ex) getObject(int.class,"10") -> Interger.valueOf(10) �� ����
	 * 
	 * </pre>
	 * 
	 * @param clazz
	 * @param value
	 * @return ������ �� ���� ��� null
	 * 
	 * @see net.ion.framework.logging.LogBroker
	 */
	public static Object getObjectInstance(Class<?> clazz, String value) throws InstanceCreationException {
		Object object = null;

		if (clazz.isPrimitive()) {
			if (clazz == int.class) {
				try {
					object = Integer.valueOf(value);
				} catch (NumberFormatException nf) { // value�� �������� �ƴϾ ������ ����� 0����
					object = new Integer(0);
				}
			} else if (clazz == boolean.class) {
				// ���� spec�� ������ "yes"�� boolean���� false������ "yes"�� ��쿡�� true�� �Ѵ�. �׿ܿ��� T,Y�� true�� �Ѵ�.
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
				// ���� spec�� ������ "yes"�� boolean���� false������ "yes"�� ��쿡�� true�� �Ѵ�. �׿ܿ��� T,Y�� true�� �Ѵ�.
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
	 *            �����ϰ��� �ϴ� className ex) java.lang.Integer , int
	 * @param value
	 *            �����ÿ� ����� value ex) 10
	 * @return
	 */
	public static Object getObjectInstance(String className, String value) throws ClassNotFoundException, InstanceCreationException {
		Class<?> cls = getClassInstance(className);
		return getObjectInstance(cls, value);
	}

	/**
	 * class�� property write method�� method name�� key�� �Ͽ� hash map���� �����.
	 * 
	 * @param clazz
	 * @return
	 * @throws InstanceCreationException
	 */
	private static HashMap<String, Method> getWriteMethodMapTable(Class<?> clazz) throws InstanceCreationException {
		try {
			// property �� set method �� tableȭ ��Ŵ
			HashMap<String, Method> methodMap = cachedMethodMapTable.get(clazz);

			if (methodMap == null) {
				BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
				PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
				methodMap = new HashMap<String, Method>();

				for (int i = 0; i < descriptors.length; ++i) {
					String propertyName = descriptors[i].getName(); // .toLowerCase(); !!!!!!!!!!!! ����!!!!
					Method method = descriptors[i].getWriteMethod();

					methodMap.put(propertyName, method);
				}

				// ������ ������ ���ϱ� ���� caching�Ѵ�~
				cachedMethodMapTable.put(clazz, methodMap);
			}

			return methodMap;
		} catch (Throwable t) {
			throw new InstanceCreationException(t);
		}
	}

	private static Hashtable<Class<?>, HashMap<String, Method>> cachedMethodMapTable = new Hashtable<Class<?>, HashMap<String, Method>>(); // write method �� cache�ؼ� introspection�� �� �ϵ���..

	/**
	 * property name�� value�� ������ bean object�� �����Ѵ�.
	 * 
	 * <p>
	 * ����: ���� property�� �ش��ϴ� names�� values�� index�� ���� ��ġ�ؾ��Ѵ�.
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
	 * bean object�� property�� �ش��ϴ� key value�� setting �Ѵ�.
	 * 
	 * <p>
	 * ����: ���� property�� �ش��ϴ� names�� values�� index�� ���� ��ġ�ؾ��Ѵ�.
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
			// property �� set method �� tableȭ ��Ŵ
			Class<?> beanClass = beanObject.getClass();
			HashMap<String, Method> methodMap = getWriteMethodMapTable(beanClass);

			String[] names = propertyNames;
			for (int i = 0; i < names.length; ++i) {
				String name = names[i]; // .toLowerCase(); ����!!!!!!!!!!!!!!
				Object value = propertyValues[i];
				Method setter = (Method) methodMap.get(name);

				if (setter == null) {
					System.out.println("could not find setter method of property name:" + name);
					continue;
				}

				try {
					setter.invoke(beanObject, new Object[] { value });
				} catch (Exception ex) { // ���� �κ��� ����ȭ �� �ʿ䰡 ����.
					if (value == null) { // primitive Ÿ�Կ� null�� ���� �־��� ��� ����� �´�. (object������ null�� �� �� ������ exception�� �߻����� �ʴ´�.)
						System.err.println("InstanceCreator.setPropertyValues():CAUTION:primitive type do not allow null.:setting passed.");
					} else { // setter�� parameter Ÿ���� ���� ���� ��� - type mismatch
						Class<?> param = setter.getParameterTypes()[0];
						if (param.isPrimitive()) {
							// setter�� argument�� primitive���̸鼭 value�� Number���̸� ���� �°� �������� ��õ�!
							// �θ� Number���̸� type�� ã�Ƽ� ��õ�!
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
	// * method parameter type�� object�� type�� üũ�� �� object ���� �����ؾ��� ��� �����Ͽ� �����Ѵ�.
	// * �̿��� ��� input�� o�� �״�� �����Ѵ�.
	// * <pre>
	// * 1. method parameter type�� primitive�� ���
	// * a. object type�� Number���� �ڼ��϶� ������ primitive wrapper object�� ��ȯ�Ͽ� ����
	// * b. object �� null�� ��� exception �߻�
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
