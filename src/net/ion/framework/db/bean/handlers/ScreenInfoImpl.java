package net.ion.framework.db.bean.handlers;

import java.util.HashMap;
import java.util.Map;

import net.ion.framework.db.Page;
import net.ion.framework.db.ScreenInfo;

public class ScreenInfoImpl implements ScreenInfo {

	private Page page;
	private int rowCountOfScreen;

	private ScreenInfoImpl(Page page, int rowCountOfScreen) {
		this.page = page;
		this.rowCountOfScreen = rowCountOfScreen;
	}

	public static ScreenInfo create(Page page, int rowCountOfScreen) {
		return new ScreenInfoImpl(page, rowCountOfScreen);
	}

	public int getRowCount() {
		return rowCountOfScreen;
	}

	public boolean hasNextScreen() {
		return page.getMaxScreenCount() < rowCountOfScreen;
	}

	public boolean hasPreScreen() {
		return page.getScreenCount() > 1;
	}

	private Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listNum", page.getListNum());
		result.put("pageNo", page.getPageNo());
		result.put("screenCount", page.getScreenCount());
		result.put("rowCountOfScreen", rowCountOfScreen);
		return result;
	}

	public String toString() {
		return toMap().toString();
	}
}
