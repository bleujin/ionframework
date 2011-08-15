// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ColInfo.java

package net.ion.framework.db.rowset;

import java.io.Serializable;

public class ColInfo implements Serializable {

	public ColInfo() {
	}

	boolean autoIncrement;
	boolean caseSensitive;
	boolean currency;
	int nullable;
	boolean signed;
	boolean searchable;
	int columnDisplaySize;
	String columnLabel;
	String columnName;
	String schemaName;
	int colPrecision;
	int colScale;
	String tableName;
	String catName;
	int colType;
	String colTypeName;
}
