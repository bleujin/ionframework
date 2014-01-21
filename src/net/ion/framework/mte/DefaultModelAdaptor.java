package net.ion.framework.mte;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.ecs.xhtml.pre;

import net.ion.framework.mte.token.Token;
import net.ion.framework.mte.util.MiniParser;
import net.ion.framework.mte.util.NestedParser;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.NumberUtil;
import net.ion.framework.util.StringUtil;

/**
 * Default implementation of the model adapter.
 * <p>
 * Does the object traversal using the "." operator. Resolved value will be checked if it is either a {@link Processor} or a {@link Callable} in which case the final resolved value is computed by calling those executable objects.
 * </p>
 * 
 * <p>
 * Inherit from this adapter if you want a slight change of this behavior and set your new adaptor on the engine {@link Engine#modelAdaptor(ModelAdaptor)} .
 * </p>
 */
public class DefaultModelAdaptor implements ModelAdaptor {

	protected Map<Class<?>, Map<String, Member>> cache = new HashMap<Class<?>, Map<String, Member>>();

	@SuppressWarnings("rawtypes")
	public Object getValue(TemplateContext context, Token token, List<String> segments, String expression) {
		Object value = traverse(segments, context.model, context.errorHandler, token);
		// if value implements both, we use the more specialized implementation
		if (value instanceof Processor) {
			value = ((Processor) value).eval(context);
		} else if (value instanceof Callable) {
			try {
				value = ((Callable) value).call();
			} catch (Exception e) {
			}
		}
		return value;
	}

	protected Object traverse(List<String> segments, Map<String, Object> model, ErrorHandler errorHandler, Token token) {
		if (segments.size() == 0) {
			return null;
		}
		String objectName = segments.get(0);
		Object value = model.get(objectName);

		value = traverse(value, segments, 1, errorHandler, token);
		return value;
	}

	protected Object traverse(Object o, List<String> attributeNames, int index, ErrorHandler errorHandler, Token token) {
		Object result;
		if (index >= attributeNames.size()) {
			result = o;
		} else {
			if (o == null) {
				return null;
			}
			String attributeName = attributeNames.get(index);
			Object nextStep = nextStep(o, attributeName, errorHandler, token);
			result = traverse(nextStep, attributeNames, index + 1, errorHandler, token);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	protected Object nextStep(Object o, String attributeName, ErrorHandler errorHandler, Token token) {
		Object result;
		if (o instanceof String) {
			errorHandler.error("no-call-on-string", token, new ModelBuilder("receiver", o.toString()).build());
			return o;
		} else if (o instanceof Map) {
			Map map = (Map) o;
			result = map.get(attributeName);
		} else {
			try {
				result = getPropertyValue(o, attributeName, token);
			} catch (Exception e) {
				errorHandler.error("property-access-error", token, new ModelBuilder("property", attributeName, "object", o, "exception", e).build());
				result = "";
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	protected Object getPropertyValue(Object o, String propertyName, Token token) {
		return getPropertyValue(o, propertyName, token.getText()) ;
	}	
	protected Object getPropertyValue(Object o, String _propertyName, String tokenText) {
		try {
			// XXX this is so strange, can not call invoke on key and value for Map.Entry, so we have to get this done like this:
			String propertyName = innerParser.parse(_propertyName, new String[]{"()"}).get(0).toString() ;
			if (o instanceof Map.Entry) {
				final Map.Entry entry = (Entry) o;
				if (propertyName.equals("key")) {
					final Object result = entry.getKey();
					return result;
				} else if (propertyName.equals("value")) {
					final Object result = entry.getValue();
					return result;
				}

			}
			boolean valueSet = false;
			Object value = null;
			Member member = null;
			final Class<?> clazz = o.getClass();
			Map<String, Member> members = cache.get(clazz);
			if (members == null) {
				members = new HashMap<String, Member>();
				cache.put(clazz, members);
			} else {
				member = members.get(propertyName);
				if (member != null) {
					if (member.getClass() == Method.class){
						return invokeMethod(o, (Method) member, _propertyName) ;
//						return ((Method) member).invoke(o);
					}
					if (member.getClass() == Field.class) {
						final Field field = (Field) member;
						if (!field.isAccessible())
							field.setAccessible(true);
						return field.get(o);
					}
				}
			}

			final String suffix = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
			Class current = clazz ;
			while((current != null) && (current != Object.class)){
				final Method[] declaredMethods = current.getDeclaredMethods();
				for (Method method : declaredMethods) {
					if (method.getName().equals("get" + suffix) || method.getName().equals("is" + suffix) || method.getName().equals(propertyName)) {
						
						if (!method.isAccessible())
							method.setAccessible(true);

						value = invokeMethod(o, method, _propertyName);
//						method.invoke(o, (Object[])null) ;
						valueSet = true;
						member = method;
						current = Object.class ;
						break;
					}
				}
				current = current.getSuperclass() ;
			}
			
			if (!valueSet) {
				final Field field = clazz.getDeclaredField(propertyName);
				if (!field.isAccessible())
					field.setAccessible(true);
				value = field.get(o);
				member = field;
				valueSet = true;
			}
			if (valueSet) {
				members.put(propertyName, member);
			}
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private static NestedParser innerParser = NestedParser.createOnlyMini() ;
	private Object invokeMethod(Object o, Method method, String _propertyName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return method.invoke(o, parseArgs(_propertyName)) ;
	}
	
	public Object[] parseArgs(String input) {
		List<Object> presult = innerParser.parse(input, new String[]{"()", ","});
		
		if (presult.size() <=1) return (Object[])null ;
		
		Object argsExpr = presult.get(1);
		if (StringUtil.isBlank(argsExpr.toString())) return (Object[])null ;
		String[] argString = (List.class.isInstance(argsExpr)) ? ((List<String>)argsExpr).toArray(new String[0]) :  new String[]{argsExpr.toString()} ;

		List<Object> result = ListUtil.newList() ; 
		if (argString.length ==0) return (Object[])null ;
		for(String arg : argString){
			String trimedArg = arg.trim();
			if(NumberUtil.isNumber(trimedArg)) result.add(Integer.parseInt(trimedArg)) ;
			else result.add(trimedArg) ;
		}
		
		return result.toArray();
	}
}
