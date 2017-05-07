package com.pgjson.domain;

import com.pgjson.postgresql.domain.jsonquery.model.CastType;
import com.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author vedadzornic
 */
public class JsonPropertyTest {

	@org.junit.Test
	public void testCreate() throws Exception {
		JsonProperty property = new JsonProperty("column", "root.array[0].value");

		assertEquals("column->'root'->'array'->0->'value'", property.asJson().toSqlString());
		assertEquals("column->'root'->'array'->0->>'value'", property.asString().toSqlString());

	}


	@Test
	public void testCast() throws Exception {
		JsonProperty property = new JsonProperty("column", "root.array[1].value").cast(CastType.BIGINT);
		assertEquals("cast(column->'root'->'array'->1->>'value' as bigint)", property.toSqlString());

	}
}
