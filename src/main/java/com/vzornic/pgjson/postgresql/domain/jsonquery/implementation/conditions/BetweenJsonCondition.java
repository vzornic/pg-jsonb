package com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.conditions;

import com.vzornic.pgjson.postgresql.domain.jsonquery.JsonQueryFragment;
import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Between json condition.
 *
 * @author vedadzornic
 */
public class BetweenJsonCondition implements JsonQueryFragment {

	private JsonProperty jsonProperty;
	private ParametrizedValue fromValue;
	private ParametrizedValue toValue;
	private boolean ignoreValues = false;
	private String operator = "BETWEEN";

	public BetweenJsonCondition(JsonProperty jsonProperty, ParametrizedValue fromValue, ParametrizedValue toValue) {
		this.jsonProperty = jsonProperty;
		this.fromValue = fromValue;
		this.toValue = toValue;
	}

	public BetweenJsonCondition ignoreValues() {
		this.ignoreValues = true;
		return this;
	}

	public BetweenJsonCondition not() {
		this.operator = "NOT BETWEEN";
		return this;
	}

	@Override
	public String toSqlString() {
		return new StringBuilder()
				.append(jsonProperty.toSqlString())
				.append(" ")
				.append(operator)
				.append(" ")
				.append(JsonbConditionUtils.createValue(fromValue, ignoreValues))
				.append(" AND ")
				.append(JsonbConditionUtils.createValue(toValue, ignoreValues))
				.toString();
	}

	@Override
	public List<ParametrizedValue> getParametrizedValues() {
		List<ParametrizedValue> result = new ArrayList<>();
		result.addAll(jsonProperty.getParametrizedValues());
		result.add(fromValue);
		result.add(toValue);
		return result;
	}
}
