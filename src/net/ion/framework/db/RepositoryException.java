package net.ion.framework.db;

import java.io.IOException;
import java.sql.SQLException;

import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.exception.FrameworkException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class RepositoryException extends FrameworkException {

	private RepositoryException(Throwable cause) {
		super(cause);
	}

	private RepositoryException(Throwable cause, String message) {
		super(cause, message);
	}

	private RepositoryException(String message) {
		super(message);
	}

	private RepositoryException(Exception ex, IQueryable query) {
		super(ex, ex.getMessage() + "\n\t: " + query.toString());
	}

	public final static RepositoryException throwIt(SQLException ex, IQueryable query) {
		throw new RepositoryException(ex, query);
	}

	public final static RepositoryException throwIt(IOException ex, IQueryable query) {
		throw new RepositoryException(ex, query);
	}

	public final static RepositoryException throwIt(Exception ex) {
		throw new RepositoryException(ex);
	}

	public final static RepositoryException throwIt(String message) {
		throw new RepositoryException(message);
	}

	public final static RepositoryException notSupportedOperation() {
		throw new RepositoryException("This Method not yet implemented.");
	}

}
