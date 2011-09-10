package net.ion.framework.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.SetUtils;

import com.mongodb.util.OrderedSet;

public class SetUtil extends SetUtils {

	public final static Set EMPTY = Collections.EMPTY_SET;

	public final static <V> Set<V> newSet() {
		return new HashSet<V>();
	}

	public final static <V> Set<V> newSyncSet() {
		return Collections.synchronizedSet(new HashSet<V>());
	}

	public final static <V> Set<V> newOrdereddSet() {
		return new OrderedSet(newSet());
	}

	public static <V> Set<V> create(V... vs) {
		Set<V> result = newSet();
		for (V value : vs) {
			result.add(value);
		}
		return result;
	}

}
