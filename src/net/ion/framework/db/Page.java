package net.ion.framework.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ion.framework.util.ListUtil;

import org.apache.log4j.lf5.StartLogFactor5;

public class Page implements Serializable{

	public final static Page ALL = new Page(1000000, 1, 1);
	public final static Page DEFAULT = new Page(100, 1, 10);
	public final static Page HUNDRED = new Page(100, 1, 10);
	public final static Page HUNDRED_ONE = new Page(101, 1, 10);
	public static final Page TEN = new Page(10, 1, 10);
	public static final Page TEN_ONE = new Page(11, 1, 10);
	private final int listNum;
	private final int _pageNo;
	private final int screenCount;

	private Page(int listNum, int pageNo, int screenCount) {
		this.listNum = (listNum < 1) ? 1 : listNum;
		this._pageNo = (pageNo < 1) ? 0 : pageNo - 1;
		this.screenCount = (screenCount > 0) ? screenCount : 0;
	}

	public final static Page create(int listNum, int pageNo) {
		return new Page(listNum, pageNo, 10);
	}

	public final static Page create(int listNum, int pageNo, int screenCount) {
		return new Page(listNum, pageNo, screenCount);
	}

	public final static Page limit(int listNum) {
		return new Page(Math.max(1, listNum), 1, 1);
	}

	public int getListNum() {
		return listNum;
	}

	public int getPageNo() {
		return _pageNo + 1;
	}

	public int getScreenCount() {
		return screenCount;
	}

	public int getStartLoc() {
		return listNum * _pageNo;
	}

	public int getEndLoc() {
		return listNum * (_pageNo + 1);
	}

	public int getMaxScreenCount() {
		return listNum * screenCount;
	}

	public int getMaxCount() {
		return listNum * screenCount * (_pageNo / 10 + 1) + 1;
	}

	public <T> List<T> subList(List<T> result) {
		if (result.size() < getStartLoc()) {
			return new ArrayList<T>();
		}

		return result.subList(getStartLoc(), Math.min(result.size(), getEndLoc()));
	}
	
	public <T> List<T> subList(Iterator<T> iter) {
		int startLoc = getStartLoc();
		int endLoc = getEndLoc() ;
		
		List<T> result = ListUtil.newList() ;
		int currLoc = 0 ;
		while(iter.hasNext()){
			T next = iter.next();
			if (currLoc >= startLoc) result.add(next) ;
			if (++currLoc >= endLoc) break ;
		}
		return result ;
	}

	
	public String str(){
		return "lnum:" + getListNum() + ", pno:" + getPageNo() + ", sco:" + getScreenCount();
	}

	public String toString() {
		return "listNum:" + getListNum() + ", pageNo:" + getPageNo();
	}

	public Page getNextPage() {
		return create(this.listNum, getPageNo() + 1, getScreenCount());
	}

	public Page getPrePage() {
		return create(this.listNum, getPageNo() - 1, getScreenCount());
	}

	public int getMaxPageNo(int rowCount) {
		//return (rowCount + listNum - 1) / getListNum();
		return ((rowCount + listNum - 1) / getListNum()) + getMinPageNoOnScreen() - 1 ;  
	}

	public int getCurrentScreen() {
		return _pageNo / screenCount + 1;
	}

	public int getMaxScreen(int rowCount) {
		return (rowCount - 1) / (listNum * screenCount) + 1;
	}

	public int getMinPageNo(int rowCount) {
		return (getMaxScreen(rowCount) - 1) * screenCount + 1;
	}

	public int getMinPageNoOnScreen() {
		//return _pageNo / screenCount * screenCount + 1;
		return ((getCurrentScreen() - 1) * screenCount) + 1;
	}

	public int getMaxPageNoOnScreen() {
		//return _pageNo / screenCount * screenCount + screenCount;
		return ((getCurrentScreen() - 1) * screenCount) + screenCount;
	}

	public int getNextScreenStartPageNo() {
		//return getCurrentScreen() * getListNum() + 1;
		return getCurrentScreen() * screenCount + 1;
	}

	public int getPreScreenEndPageNo() {
		//return (getCurrentScreen() - 1) * getListNum();
		return (getCurrentScreen() - 1) * screenCount;
	}

	public int getSkipOnScreen() {
		return getScreenCount() * (getCurrentScreen() - 1) * getListNum();
	}

	public int getOffsetOnScreen() {
		return getScreenCount() * getListNum() + 1 ;
	}


}
