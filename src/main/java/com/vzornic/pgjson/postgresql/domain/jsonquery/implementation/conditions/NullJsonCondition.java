package com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.conditions;

import com.vzornic.pgjson.postgresql.domain.jsonquery.JsonQueryFragment;
import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

import java.util.Collections;
import java.util.List;

public class NullJsonCondition implements JsonQueryFragment {

	protected JsonProperty jsonProperty;
	protected String operator;

	public NullJsonCondition(JsonProperty jsonProperty, boolean isNull) {
		this.jsonProperty = jsonProperty;

		if (isNull) {
			operator = " IS NULL ";
		} else {
			operator = " IS NOT NULL ";
		}
	}


	public String toSqlString() {
		StringBuilder result = new StringBuilder();
		result.append(jsonProperty.toSqlString());
		result.append(operator);
		return result.toString();
	}

	@Override
	public List<ParametrizedValue> getParametrizedValues() {
		return Collections.emptyList();
	}
}
