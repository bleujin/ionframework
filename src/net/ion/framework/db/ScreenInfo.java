package net.ion.framework.db;

import java.io.Serializable;

public interface ScreenInfo extends Serializable {

	public final static ScreenInfo UNKNOWN = new ScreenInfo() {
		public int getRowCount() {
			return -1;
		}

		public boolean hasNextScreen() {
			return false;
		}

		public boolean hasPreScreen() {
			return false;
		}
	};

	public int getRowCount();

	public boolean hasNextScreen();

	public boolean hasPreScreen();

}
