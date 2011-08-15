package net.ion.framework.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.sql.RowSet;

import net.ion.framework.configuration.Configuration;
import net.ion.framework.configuration.ConfigurationException;
import net.ion.framework.configuration.NotFoundXmlTagException;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.cache.CacheConfigImpl;
import net.ion.framework.db.constant.RuntimeService;
import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.MSSQLPoolDBManager;
import net.ion.framework.db.procedure.IBatchQueryable;
import net.ion.framework.db.procedure.ICombinedUserProcedures;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.db.procedure.RepositoryService;
import net.ion.framework.db.servant.AfterTask;
import net.ion.framework.db.servant.ExtraServant;
import net.ion.framework.db.servant.NoneServant;
import net.ion.framework.db.servant.PrimaryServant;
import net.ion.framework.db.servant.ServantChannel;
import net.ion.framework.db.servant.StdOutServant;
import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.Debug;
import net.ion.framework.util.InstanceCreationException;
import net.ion.framework.util.InstanceCreator;
import net.ion.framework.util.StringUtil;

import org.apache.commons.io.IOUtils;

/**
 * Front Facade Role Used Facade Pattern .execUpdate, .execQuery(String DynamicQuery) .execCommandUpdate, .execCommandQuery(Parameter Query) .execProcedureUpdate, .execProcedureQuery(Procedure Query & Lob Type Query)
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

public class DBController implements IDBController { // implements Configurable
// public static DBController TEST_CONTROLLER = new DBController("test", new OracleCacheDBManager("jdbc:oracle:thin:@bleujin:1521:bleujin", "ipub40","ics4_r3" ,5), new StdOutServant(StdOutServant.All)) ;
	public static DBController TEST_CONTROLLER = new DBController("test", new MSSQLPoolDBManager(
			"jdbc:microsoft:sqlserver://dev_sql.i-on.net:1433;DatabaseName=HK_NEWS_ICS5", "HK_NEWS_ICS5", "HK_NEWS_ICS5"), new StdOutServant(StdOutServant.All));
	protected DBManager dbm = null;
	protected int limitRows = Page.ALL.getListNum();
	protected String name = null;
	private ServantChannel echannel = null;

	private HashMap<String, String> midgard = new CaseInsensitiveHashMap<String>(); // etc property...
	private PrimaryServant pservant = null;

	private static Logger log = LogBroker.getLogger(DBController.class);
	private long modify_count = 0;

	public DBController(Configuration dbconfig) throws DBControllerInstantiationException {

		if (!dbconfig.getTagName().equals("database-controller"))
			throw new DBControllerInstantiationException("invalid configuration.");

		try {
			// π›µÂΩ√ « ø‰«— ∞ÕµÈ...
			this.name = dbconfig.getChild("controller-name").getValue();
			this.dbm = (DBManager) InstanceCreator.createConfiguredInstance(dbconfig.getChild("database-manager.configured-object"));

			Configuration[] configOfServant = dbconfig.getChildren("extra-servant.configured-object");
			ExtraServant eservant = null;

			// None Channel...
			if (configOfServant.length == 0) {
				echannel = new ServantChannel(new NoneServant());
			} else {
				for (int i = 0; i < configOfServant.length; i++) {
					if (i == 0) {
						eservant = (ExtraServant) InstanceCreator.createConfiguredInstance(configOfServant[i]);
						echannel = new ServantChannel(eservant);
						eservant.setChannel(echannel);
					} else {
						ExtraServant nextServant = (ExtraServant) InstanceCreator.createConfiguredInstance(configOfServant[i]);
						eservant.setNext(nextServant);
						eservant = nextServant;
					}
				}
			}
			log.info(this.name + " [---DBController Start---] ..............");

			// ¿÷¿∏∏È ¡¡∞Ì æ¯¿∏∏È ±◊∏∏...
			try {
				this.limitRows = dbconfig.getChild("limit-rows").getValueAsInt();
			} catch (NotFoundXmlTagException ignore) {
			}

			// -_- for indexer..
			try {
				Configuration configOfPServant = dbconfig.getChild("primary-servant.configured-object");
				if (configOfPServant != null) {
					pservant = (PrimaryServant) InstanceCreator.createConfiguredInstance(configOfPServant);
				}

			} catch (NotFoundXmlTagException ex1) {
			}

			// midgard
			try {
				Configuration[] mconfig = dbconfig.getChildren("midgard.property");
				for (int i = 0, last = mconfig.length; i < last; i++) {
					midgard.put(mconfig[i].getAttribute("key"), mconfig[i].getAttribute("value"));
				}
			} catch (NotFoundXmlTagException ex) {
			}

			// cache wrapper
			try {
				String relativePath = dbconfig.getChild("cache-config-file").getValue();
				String cacheConfigString = IOUtils.toString(new FileInputStream(relativePath), "UTF-8");
				CacheConfigImpl cacheConfig = new CacheConfigImpl(cacheConfigString);
				CacheDBManager cdbm = new CacheDBManager(cacheConfig, this.dbm);
				this.dbm = cdbm;

			} catch (NotFoundXmlTagException ex) {
			} catch (IOException e) {
				Debug.debug("cause " + e.getMessage(), "cacheManager not setted..");
				e.printStackTrace();
			}
		} catch (NotFoundXmlTagException ex) {
			throw new DBControllerInstantiationException("Few parameter to initialize DBController.", ex);
		} catch (ConfigurationException e) {
			throw new DBControllerInstantiationException("unexpected exception.", e);
		} catch (InstanceCreationException ice) {
			throw new DBControllerInstantiationException("could not instantiate database manager. ", ice);
		}
	}

	public DBController(DBManager dataBaseManager) {
		this("Anonymous controller", dataBaseManager);
	}

	/**
	 * @param name
	 *            controller's name : recognize dbc, but dont need unique name
	 * @param dataBaseManager
	 */
	public DBController(String name, DBManager dbm) {
		this(name, dbm, null);
	}

	public DBController(String name, DBManager dataBaseManager, ExtraServant servant) {
		this.name = name;
		this.dbm = dataBaseManager;
		this.echannel = new ServantChannel(servant);

		if (servant != null)
			servant.setChannel(this.echannel); // ÔøΩﬂ∞ÔøΩÔøΩÔøΩ ÔøΩÔøΩ
	}

	public void setDBManager(DBManager dbm) {
		// before pool destroy
		try {
			if (this.dbm != null)
				this.dbm.destroyPool(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		log.info(this.name + " DBController Change DBManager[" + dbm.toString() + "]");
		this.dbm = dbm;
	}

	public void setServantChannel(ServantChannel channel) {
		this.echannel.stopServant();

		log.info(this.name + " DBController Change Servant[" + channel.toString() + "]");
		this.echannel = channel;
	}

	public void setMidgardProperty(String key, String value) {
		log.info(this.name + " DBController Midgard Property[" + key + " : " + value + "]");
		midgard.put(key, value);
	}

	public ExtraServant getServant() {
		return this.echannel.getExtraServant();
	}

	private boolean initialized = false;

	public void initSelf() throws SQLException {
		this.initSelf(new RuntimeService());
	}

	public synchronized void initSelf(RuntimeService rservice) throws SQLException {
		if (initialized)
			return;

		getDBManager().initPool(this);
		echannel.restartWorker();
		this.initialized = true;
	}

	public String getMidgardProperty(String key) {
		return (String) midgard.get(key);
	}

	public int getMidgardPropertyAsInt(String key, int defaultValue) {
		String str = (String) midgard.get(key);
		return StringUtil.isNumeric(str) ? Integer.parseInt(str) : defaultValue;
	}

	public DBManager getDBManager() {
		return this.dbm;
	}

	public RepositoryService getService() {
		return this.dbm.getRepositoryService();
	}

	public int getLimitedRows() {
		return this.limitRows;
	}

	public void setLimitedRows(int i) {
		this.limitRows = i;
	}

	public String getName() {
		return this.name;
	}

	public synchronized void destroySelf() {
		echannel.stopServant();
		try {
			if (dbm != null)
				dbm.destroyPool(this);
		} catch (NullPointerException ignore) {
			// already closed
			log.warning("Destroy of DBController Failed(NullPointer) : " + ignore.getMessage());
		} catch (SQLException ignore) {
			log.warning("Destroy of DBController Failed : " + ignore.getMessage());
		}
		log.info("DBController : destroyed");
		initialized = false;
	}

	// FOR TEST
	public synchronized void changeServant(ExtraServant servant) {
		this.echannel.stopServant();
		this.echannel = new ServantChannel(servant);
		servant.setChannel(this.echannel);
		this.echannel.startWorker();
	}

	public void addServant(ExtraServant addServant) {
		echannel.addServant(addServant);
	}

	private Connection getConnection() {
		try {
			return dbm.getConnection();
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	private void freeConnection(Connection conn) {
		try {
			dbm.freeConnection(conn);
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	public void freeConnection(Connection conn, RowSet rs) {
		try {
			rs.close();
			dbm.freeConnection(conn);
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	public int getDBManagerType() {
		return dbm.getDBManagerType();
	}

	// Standard Query & Update
	public int execUpdate(String strSQL) throws SQLException {
		IUserCommand comd = createUserCommand(strSQL);
		return comd.execUpdate();
	}

	public int execUpdate(Queryable query) {

		try {
			Queryable _query = query;
			if (pservant != null) {
				_query = pservant.addQuery(getDBManager(), query);
			}

			int result = _query.execUpdate();
			return result;
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex, query);
		}
	}

	public Rows execQuery(String strSQL) {
		IUserCommand comd = createUserCommand(strSQL);
		try {
			return comd.execQuery();
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e, comd);
		}
	}

	public void handleServant(long start, long end, IQueryable query, int execType) {
		synchronized (this) {
			if (execType == IQueryable.UPDATE_COMMAND)
				modify_count++;
		}

		if (query instanceof IUserProcedures) {
			IUserProcedures uprocs = (IUserProcedures) query;
			for (int i = 0, last = uprocs.size(); i < last; i++) {
				handleServant(start, end, uprocs.getQuery(i), execType);
			}
		} else {
			try {

				echannel.putTask(new AfterTask(start, end, this.dbm, query, execType));
			} catch (InterruptedException ex) {
				log.warning("channel is interrupted..");
				ex.printStackTrace();
			}
		}
	}

	public IUserProcedure createUserProcedure(String procSQL) {
		IUserProcedure upt = getService().createUserProcedure(this, procSQL);
		upt.setPage(Page.create(getLimitedRows(), 1));
		return upt;
	}

	public IParameterQueryable createParameterQuery(String procSQL) {
		IParameterQueryable upt = getService().createParameterQuery(this, procSQL);
		upt.setPage(Page.create(getLimitedRows(), 1));
		return upt;
	}

	public IBatchQueryable createBatchParameterQuery(IDBController dc, String strSQL) {
		IBatchQueryable upt = getService().createBatchParameterQuery(this, strSQL);
		upt.setPage(Page.create(getLimitedRows(), 1));
		return upt;
	}

	public IUserCommand createUserCommand(String procSQL) {
		IUserCommand upt = getService().createUserCommand(this, procSQL);
		upt.setPage(Page.create(getLimitedRows(), 1));
		return upt;
	}

	public IUserCommandBatch createUserCommandBatch(String procSQL) {
		return getService().createUserCommandBatch(this, procSQL);
	}

	public IUserProcedureBatch createUserProcedureBatch(String procSQL) {
		return getService().createUserProcedureBatch(this, procSQL);
	}

	public IUserProcedures createUserProcedures(String procSQL) {
		return getService().createUserProcedures(this, procSQL);
	}

	public ICombinedUserProcedures createCombinedUserProcedures(String name) {
		return getService().createCombinedUserProcedures(this, name);
	}

	public long getModifyCount() {
		return modify_count;
	}

	public DatabaseMetaData getDatabaseMetaData() throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection();
			return conn.getMetaData();
		} finally {
			try {
				freeConnection(conn);
			} catch (Exception ex1) {
			}
		}
	}

	public int execUpdate(IQueryable upt) {
		try {
			return upt.execUpdate();
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e);
		}
	}

	public Rows getRows(IQueryable query) {
		try {
			return query.execQuery();
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e);
		}
	}

	public Object execHandlerQuery(IQueryable query, ResultSetHandler handler) {
		try {
			return query.execHandlerQuery(handler);
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e);
		}
	}

	public Rows getRows(String query) {
		return getRows(createUserCommand(query));
	}

	public Rows getRows(String query, Page page) {
		IQueryable iquery = createUserCommand(query);
		iquery.setPage(page);
		return getRows(iquery);
	}

	public void setPrimaryServant(PrimaryServant pservant) {
		this.pservant = pservant;

	}

	public void setMidgard(HashMap<String, String> midgard) {
		this.midgard = midgard;
	}
}
