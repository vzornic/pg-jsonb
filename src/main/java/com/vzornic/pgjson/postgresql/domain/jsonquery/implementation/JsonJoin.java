package com.vzornic.pgjson.postgresql.domain.jsonquery.implementation;

import com.vzornic.pgjson.postgresql.domain.jsonquery.JsonQueryFragment;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

import java.util.List;

/**
 * TODO this class might not be necessary. Double check !!
 * @author vedadzornic
 */
public class JsonJoin implements JsonQueryFragment {
	protected JsonProperty property;
	protected JsonProperty joinProperty;
	protected String joinTable;
	protected String joinOperator;

	public JsonJoin(JsonProperty property, JsonProperty joinProperty, String joinTable) {
		this(property, joinProperty, joinTable, "=");
	}

	public JsonJoin(JsonProperty property, JsonProperty joinProperty, String joinTable, String joinOperator) {
		this.property = property;
		this.joinProperty = joinProperty;
		this.joinTable = joinTable;
		this.joinOperator = joinOperator;
	}

	public String toSqlString() {
		StringBuilder result = new StringBuilder();
		result.append(joinTable)
				.append(" ON ")
				.append(property.toSqlString())
				.append(joinOperator)
				.append(joinProperty.toSqlString());
		return result.toString();

	}

	@Override
	public List<ParametrizedValue> getParametrizedValues() {
		return null;
	}
}
