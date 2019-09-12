package com.vzornic.pgjson.domain;

import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.JsonIteratorMode;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.CastType;
import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author vedadzornic
 */
public class JsonPropertyJSONBExpressionTest {

	@org.junit.Test
	public void testCreate() throws Exception {
		JsonProperty property = new JsonProperty("column", "root.array[0].value");
		assertEquals("column#>'{root,array,0,value}'", property.asJson().toSqlString());
		assertEquals("column#>>'{root,array,0,value}'", property.asString().toSqlString());

		property.mode(JsonIteratorMode.ARROW_ITERATOR_MODE);
		assertEquals("column->'root'->'array'->0->'value'", property.asJson().toSqlString());
		assertEquals("column->'root'->'array'->0->>'value'", property.asString().toSqlString());

	}


	@Test
	public void testCast() throws Exception {
		JsonProperty property = new JsonProperty("column", "root.array[1].value").cast(CastType.BIGINT);
		assertEquals("cast(column#>>'{root,array,1,value}' as bigint)", property.toSqlString());

		property.mode(JsonIteratorMode.ARROW_ITERATOR_MODE);
		assertEquals("cast(column->'root'->'array'->1->>'value' as bigint)", property.toSqlString());


	}
}
