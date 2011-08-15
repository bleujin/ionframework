package net.ion.framework.session;

import java.io.Serializable;
import java.util.Locale;

public class SerializedUserBean implements Serializable {
	private String userId = "";
	private String userNm = "";
	private String langCd = "";
	private Locale locale;
	private String email = "";
	private String contextURL = "";

	public SerializedUserBean() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLangCd() {
		return langCd;
	}

	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public boolean equals(Object obj) {
		if (obj != null) {
			if (userId.equals(((SerializedUserBean) obj).getUserId()) && userNm.equals(((SerializedUserBean) obj).getUserNm())
					&& langCd.equals(((SerializedUserBean) obj).getLangCd()) && email.equals(((SerializedUserBean) obj).getEmail())) {
				return true;
			}
		}
		return false;
	}

	public String getContextURL() {
		return contextURL;
	}

	public void setContextURL(String contextURL) {
		this.contextURL = contextURL;
	}

}
