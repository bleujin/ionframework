package net.ion.framework.parse.gson;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NotFoundJsonElement extends JsonElement {

	
	public final static NotFoundJsonElement NOT_FOUND = new NotFoundJsonElement() ;

	private NotFoundJsonElement(){}
	
	public boolean isJsonArray() {
		return false;
	}

	public boolean isJsonObject() {
		return false;
	}

	public boolean isJsonPrimitive() {
		return false;
	}

	public boolean isJsonNull() {
		return true;
	}

	public JsonObject getAsJsonObject() {
		return null ;
	}

	public JsonArray getAsJsonArray() {
		return null ;
	}

	public JsonPrimitive getAsJsonPrimitive() {
		return null ;
	}

	public JsonNull getAsJsonNull() {
		return null ;
	}

	public boolean getAsBoolean() {
		return false ;
	}

	Boolean getAsBooleanWrapper() {
		return null ;
	}

	public Number getAsNumber() {
		return null ;
	}

	public String getAsString() {
		return null ;
	}

	public double getAsDouble() {
		return 0.0d ;
	}

	public float getAsFloat() {
		return 0.0f ;
	}

	public long getAsLong() {
		return 0L ;
	}

	public int getAsInt() {
		return 0 ;
	}

	public byte getAsByte() {
		return '\0' ;
	}

	public char getAsCharacter() {
		return '\0' ;
	}

	public BigDecimal getAsBigDecimal() {
		return null ;
	}

	public BigInteger getAsBigInteger() {
		return null ;
	}

	public short getAsShort() {
		return 0 ;
	}

}
