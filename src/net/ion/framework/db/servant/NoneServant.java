package net.ion.framework.db.servant;

public class NoneServant extends ExtraServant {
	public NoneServant() {
	}

	protected void handle(AfterTask atask) {
	}

	protected boolean isDealWith(AfterTask atask) {
		return true;
	}

	public ExtraServant newCloneInstance() {
		return new NoneServant();
	}

}
