package net.ion.framework.db.cache;

import java.util.ArrayList;
import java.util.List;

import net.ion.framework.parse.gson.JsonArray;
import net.ion.framework.parse.gson.JsonElement;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.StringUtil;

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
		JsonObject root = JsonParser.fromString(configString).getAsJsonObject();

		if ( (! root.has("cache")) || (! root.get("cache").isJsonArray()) || root.asJsonArray("cache").size() == 0)
			return;

		JsonArray group = root.asJsonArray("cache");

		for (JsonElement jele : group.toArray()) {
			if (! jele.isJsonObject()) continue ;
			JsonObject jobj = (JsonObject) jele ;
			String groupId = jobj.asString("groupId");

			int count = jobj.asInt("count");
			JsonArray adds = jobj.asJsonArray("add");
			JsonArray resets = jobj.asJsonArray("reset");

			CacheGroup cgroup = new CacheGroup(groupId, count);
			for (JsonElement addEle : adds.toArray()) {
				cgroup.addProc(addEle.getAsString());
			}

			for (JsonElement resetEle : resets.toArray()) {
				cgroup.resetProc(resetEle.getAsString());
			}

			this.addGroup(cgroup);
		}
	}

	public Cache build(CacheManager cm) {
		return new CacheImpl(this, groups, cm);
	}

	private void addGroup(CacheGroup cgroup) {
		groups.add(cgroup);
	}

	public CacheGroup[] getGroups() {
		return groups.toArray(new CacheGroup[0]);
	}

}
