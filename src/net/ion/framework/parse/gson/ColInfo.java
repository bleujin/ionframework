// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ColInfo.java

package net.ion.framework.parse.gson;

import java.io.Serializable;

public class ColInfo implements Serializable {

	private boolean autoIncrement = false;
	private boolean caseSensitive = false;
	private boolean currency = false;
	private int nullable = 0;
	private boolean signed;
	private boolean searchable;
	private int columnDisplaySize;
	
	private String columnLabel;
	private String columnName;
	private String tableName = "";
	
	private String schemaName;
	private int colPrecision;
	private int colScale;
	private String catName;
	private int colType;
	private String colTypeName;
	
	private final RowsMetaDataImpl meta;

	ColInfo(RowsMetaDataImpl meta, String label) {
		this.meta = meta ;
		this.columnLabel = label ;
		this.columnName = label ;
	}

	public boolean autoIncrement() {
		return autoIncrement;
	}

	public boolean caseSensitive() {
		return caseSensitive;
	}

	public boolean currency() {
		return currency;
	}

	public int nullable() {
		return nullable;
	}

	public boolean signed() {
		return signed;
	}

	public boolean searchable() {
		return searchable;
	}

	public int columnDisplaySize() {
		return columnDisplaySize;
	}

	public String columnLabel() {
		return columnLabel;
	}

	public String columnName() {
		return columnName;
	}

	public String schemaName() {
		return schemaName;
	}

	public int colPrecision() {
		return colPrecision;
	}

	public int colScale() {
		return colScale;
	}

	public String tableName() {
		return tableName;
	}

	public String catName() {
		return catName;
	}

	public int colType() {
		return colType;
	}

	public String colTypeName() {
		return colTypeName;
	}

	
	
	
	public RowsMetaDataImpl parent(){
		return meta ;
	}
	
	public ColInfo autoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
		return this ;
	}

	public ColInfo caseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		return this ;
	}

	public ColInfo currency(boolean currency) {
		this.currency = currency;
		return this ;
	}

	public ColInfo nullable(int nullable) {
		this.nullable = nullable;
		return this ;
	}

	public ColInfo signed(boolean signed) {
		this.signed = signed;
		return this ;
	}

	public ColInfo searchable(boolean searchable) {
		this.searchable = searchable;
		return this ;
	}

	public ColInfo columnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
		return this ;
	}

	public ColInfo columnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
		return this ;
	}

	public ColInfo columnName(String columnName) {
		this.columnName = columnName;
		return this ;
	}

	public ColInfo schemaName(String schemaName) {
		this.schemaName = schemaName;
		return this ;
	}

	public ColInfo colPrecision(int colPrecision) {
		this.colPrecision = colPrecision;
		return this ;
	}

	public ColInfo colScale(int colScale) {
		this.colScale = colScale;
		return this ;
	}

	public ColInfo tableName(String tableName) {
		this.tableName = tableName;
		return this ;
	}

	public ColInfo catName(String catName) {
		this.catName = catName;
		return this ;
	}

	public ColInfo colType(int colType) {
		this.colType = colType;
		return this ;
	}

	public ColInfo colTypeName(String colTypeName) {
		this.colTypeName = colTypeName;
		return this ;
	}

	
}
