package net.ion.framework.db.cache;

public interface CacheConfig {

	public enum FoundType {
		ADD, RESET, NOT_DEFINED;

		public boolean isAdd() {
			return this.equals(ADD);
		}

		public boolean isReset() {
			return this.equals(RESET);
		}

		public boolean isNotDefined() {
			return this.equals(NOT_DEFINED);
		}
	}

	public Cache build(CacheManager cm);
}
