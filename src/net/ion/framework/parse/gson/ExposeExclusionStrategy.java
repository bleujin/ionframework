package net.ion.framework.parse.gson;

import net.ion.framework.parse.gson.annotations.Expose;

public class ExposeExclusionStrategy implements ExclusionStrategy {

	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes field) {
		Expose annotation = field.getAnnotation(Expose.class);
		if (annotation == null)
			return false;

		return annotation.serialize();
	}

}
