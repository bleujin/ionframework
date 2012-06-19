package net.ion.framework.util.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

public interface EmSerializable {
	public int getSerializedSize();

	public void writeSelf(OutputStream out) throws IOException;

	public void readSelf(InputStream in) throws IOException;
}
