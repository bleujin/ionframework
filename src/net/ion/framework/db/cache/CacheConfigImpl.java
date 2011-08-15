package net.ion.framework.db.cache;

import java.util.ArrayList;
import java.util.List;

import net.ion.framework.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

// Thread Unsafe
// not modify in running 
public class CacheConfigImpl implements CacheConfig {

	private List<CacheGroup> groups = new ArrayList<CacheGroup>();

	public CacheConfigImpl(String configString) {
		init(configString);
	}

	private void init(String configString) {
		if (StringUtil.isBlank(configString))
			return;
		JSONObject root = JSONObject.fromObject(configString);

		if (root.isEmpty() || root.isNullObject())
			return;

		JSONArray group = root.getJSONArray("cache");

		for (int i = 0, last = group.size(); i < last; i++) {
			JSONObject jobj = group.getJSONObject(i);
			String groupId = jobj.getString("groupId");

			int count = jobj.getInt("count");
			JSONArray adds = jobj.getJSONArray("add");
			JSONArray resets = jobj.getJSONArray("reset");

			CacheGroup cgroup = new CacheGroup(groupId, count);
			for (int k = 0, klast = adds.size(); k < klast; k++) {
				cgroup.addProc(adds.getString(k));
			}

			for (int m = 0, mlast = resets.size(); m < mlast; m++) {
				cgroup.resetProc(resets.getString(m));
			}

			this.addGroup(cgroup);
		}
	}

	public Cache getCache(CacheManager cm) {
		return new CacheImpl(this, groups, cm);
	}

	private void addGroup(CacheGroup cgroup) {
		groups.add(cgroup);
	}

	public CacheGroup[] getGroups() {
		return groups.toArray(new CacheGroup[0]);
	}

}
