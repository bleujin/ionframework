package net.ion.framework.db.cache;

import java.util.Set;

import net.ion.framework.db.cache.CacheConfig.FoundType;
import net.ion.framework.util.CaseInsensitiveSet;
import net.ion.framework.util.StringUtil;

public class CacheGroup {

	private String groupId;
	private int count;
	private Set<String> adds = new CaseInsensitiveSet<String>();
	private Set<String> resets = new CaseInsensitiveSet<String>();

	public CacheGroup(String groupId, int count) {
		this.groupId = makeKeyable(groupId);
		this.count = count;
	}

	public void addProc(String addProc) {
		adds.add(makeKeyable(addProc));
	}

	public void resetProc(String resetProc) {
		resets.add(makeKeyable(resetProc));
	}

	public String[] getAddProcs() {
		return adds.toArray(new String[0]);
	}

	public String[] getResetProcs() {
		return resets.toArray(new String[0]);
	}

	public FoundType contains(String procName) {
		if (containsResets(procName))
			return FoundType.RESET;
		if (containsAdds(procName))
			return FoundType.ADD;
		return FoundType.NOT_DEFINED;
	}

	public boolean containsAdds(String procName) {
		return adds.contains(makeKeyable(procName));
	}

	public boolean containsResets(String procName) {
		return resets.contains(makeKeyable(procName));
	}

	public String getId() {
		return groupId;
	}

	public int getCacheSize() {
		return count;
	}

	private String makeKeyable(String procName) {
		String result = StringUtil.substringBefore(StringUtil.deleteWhitespace(procName.toUpperCase()) + "(", "(");
		return result;
	}
}
