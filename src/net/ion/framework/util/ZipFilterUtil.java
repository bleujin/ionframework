package net.ion.framework.util;

import java.io.File;

import net.ion.framework.util.ZipUtil.ZipFilter;

/**
 * Link filter utilities for AND, OR, NOT and XOR.
 * 
 * @author bleujin
 * @version $Revision: 1.9 $
 */
public final class ZipFilterUtil {

	/**
	 * Disallow creation of utility class.
	 */
	private ZipFilterUtil() {
	}

	public static ZipFilter and(ZipFilter filter1, ZipFilter filter2) {
		return new And(new ZipFilter[] { filter1, filter2 });
	}

	public static ZipFilter and(ZipFilter[] filters) {
		return new And(filters);
	}

	private static class And implements ZipFilter {

		private ZipFilter[] filters;

		public And(ZipFilter[] filters) {
			this.filters = filters;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean allow(File file) {
			for (ZipFilter filter : filters) {
				if (filter == null)
					continue;
				if (filter.allow(file) == false) {
					return false;
				}
			}
			return true;
		}

	}

	public static ZipFilter or(ZipFilter filter1, ZipFilter filter2) {
		return new Or(new ZipFilter[] { filter1, filter2 });
	}

	public static ZipFilter or(ZipFilter[] filters) {
		return new Or(filters);
	}

	private static class Or implements ZipFilter {

		private ZipFilter[] filters;

		public Or(ZipFilter[] filters) {
			this.filters = filters;
		}

		public boolean allow(File file) {
			for (int i = 0; i < filters.length; i++) {
				if (filters[i].allow(file)) {
					return true;
				}
			}
			return false;
		}

	}

	// --- NOT LinkFilter ---
	public static ZipFilter not(ZipFilter filter) {
		return new Not(filter);
	}

	private static class Not implements ZipFilter {

		private ZipFilter filter;

		public Not(ZipFilter filter) {
			this.filter = filter;
		}

		public boolean allow(File file) {
			return !filter.allow(file);
		}

	}

	public static ZipFilter xor(ZipFilter filter1, ZipFilter filter2) {
		return new Xor(filter1, filter2);
	}

	private static class Xor implements ZipFilter {

		private ZipFilter f1;
		private ZipFilter f2;

		public Xor(ZipFilter f1, ZipFilter f2) {
			this.f1 = f1;
			this.f2 = f2;
		}

		public boolean allow(File file) {
			final boolean acceptF1 = f1.allow(file);
			final boolean acceptF2 = f2.allow(file);

			return ((acceptF1 && !acceptF2) || (!acceptF1 && acceptF2));
		}

	}

}
