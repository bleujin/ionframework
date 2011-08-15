package net.ion.framework.db.bean.test;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class ArticleBean {
	private int artId;
	private int modSerNo;
	private String artSubject;
	private String artCont;

	public ArticleBean() {
	}

	public String getArtCont() {
		return artCont;
	}

	public int getArtId() {
		return artId;
	}

	public String getArtSubject() {
		return artSubject;
	}

	public int getModSerNo() {
		return modSerNo;
	}

	public void setModSerNo(int modSerNo) {
		this.modSerNo = modSerNo;
	}

	public void setArtSubject(String artSubject) {
		this.artSubject = artSubject;
	}

	public void setArtId(int artId) {
		this.artId = artId;
	}

	public void setArtCont(String artCont) {
		this.artCont = artCont;
	}

}
