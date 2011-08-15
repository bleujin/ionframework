package net.ion.framework.util;

import java.util.zip.ZipEntry;

import net.ion.framework.util.ZipUtil.UnZipFilter;

public class UnZipFilterUtil {

	public static UnZipFilter and(UnZipFilter filter1, UnZipFilter filter2) {
		return new And(new UnZipFilter[] { filter1, filter2 });
	}

	public static UnZipFilter and(UnZipFilter[] filters) {
		return new And(filters);
	}

	private static class And implements UnZipFilter {

		private UnZipFilter[] filters;

		public And(UnZipFilter[] filters) {
			this.filters = filters;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean allow(ZipEntry entry) {
			for (UnZipFilter filter : filters) {
				if (filter == null)
					continue;
				if (filter.allow(entry) == false) {
					return false;
				}
			}
			return true;
		}

	}

	public static UnZipFilter or(UnZipFilter filter1, UnZipFilter filter2) {
		return new Or(new UnZipFilter[] { filter1, filter2 });
	}

	public static UnZipFilter or(UnZipFilter[] filters) {
		return new Or(filters);
	}

	private static class Or implements UnZipFilter {

		private UnZipFilter[] filters;

		public Or(UnZipFilter[] filters) {
			this.filters = filters;
		}

		public boolean allow(ZipEntry entry) {
			for (int i = 0; i < filters.length; i++) {
				if (filters[i].allow(entry)) {
					return true;
				}
			}
			return false;
		}

	}

	// --- NOT LinkFilter ---
	public static UnZipFilter not(UnZipFilter filter) {
		return new Not(filter);
	}

	private static class Not implements UnZipFilter {

		private UnZipFilter filter;

		public Not(UnZipFilter filter) {
			this.filter = filter;
		}

		public boolean allow(ZipEntry entry) {
			return !filter.allow(entry);
		}

	}

	public static UnZipFilter xor(UnZipFilter filter1, UnZipFilter filter2) {
		return new Xor(filter1, filter2);
	}

	private static class Xor implements UnZipFilter {

		private UnZipFilter f1;
		private UnZipFilter f2;

		public Xor(UnZipFilter f1, UnZipFilter f2) {
			this.f1 = f1;
			this.f2 = f2;
		}

		public boolean allow(ZipEntry entry) {
			final boolean acceptF1 = f1.allow(entry);
			final boolean acceptF2 = f2.allow(entry);

			return ((acceptF1 && !acceptF2) || (!acceptF1 && acceptF2));
		}

	}

}
