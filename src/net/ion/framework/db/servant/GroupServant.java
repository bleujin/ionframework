package net.ion.framework.db.servant;

import java.util.List;

public interface GroupServant {
	public List<IExtraServant> getServantList() ;
	public void stopServant() ;
}
