package net.ion.framework.parse.gson;

import org.apache.ecs.xhtml.form;

import junit.framework.TestCase;

public class TestSpeed extends TestCase {

	public void testSpeed() {
		for (int i = 0 ; i < 1000000 ; i++) {
			JsonObject.fromString("{\"properties\":{\"/name/last\":[\"3\"],\"/name/first\":[\"1\",\"2\"]},\"references\":{\"__transaction\":[\"/__transactions/52315bc4198e2b58945d759c\"]}}") ;
		}
	}
}
