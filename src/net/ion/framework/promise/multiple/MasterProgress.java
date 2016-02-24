package net.ion.framework.promise.multiple;

/**
 * Progress indicating how many promises need to finish ({@link #total}),
 * and how many had already finish ({@link #fulfilled}).
 */
public class MasterProgress {
	private final int done;
	private final int fail;
	private final int total;
	
	public MasterProgress(int done, int fail, int total) {
		super();
		this.done = done;
		this.fail = fail;
		this.total = total;
	}

	public int getDone() {
		return done;
	}

	public int getFail() {
		return fail;
	}

	public int getTotal() {
		return total;
	}

	@Override
	public String toString() {
		return "MasterProgress [done=" + done + ", fail=" + fail
				+ ", total=" + total + "]";
	}
}