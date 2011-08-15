package net.ion.framework.rest;

import java.util.List;
import java.util.Map;

public class IData {

	private Map<String, Object> req;
	private List<Map<String, Object>> nodes;
	private Map<String, Object> res;

	private IData(Map<String, Object> req, List<Map<String, Object>> nodes, Map<String, Object> res) {
		this.req = req;
		this.nodes = nodes;
		this.res = res;
	}

	public final static IData create(Map<String, Object> req, List<Map<String, Object>> nodes, Map<String, Object> res) {
		return new IData(req, nodes, res);
	}

	public IRequest getRequest() {
		return IRequest.create(req);
	}

	public IResponse getResponse() {
		return IResponse.create(res);
	}

	public List<Map<String, Object>> getNodes() {
		return nodes;
	}
}
