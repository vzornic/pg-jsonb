package com.pgjson.postgresql.domain.jsonquery.implementation.conditions;

import com.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import com.pgjson.postgresql.domain.jsonquery.JsonQueryFragment;
import com.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vedadzornic
 */
public class SimpleJsonCondition implements JsonQueryFragment {

	protected JsonProperty jsonProperty;
	protected ParametrizedValue value;
	protected String operator;
	protected boolean ignoreValues = false;
	private boolean lowerCase = false;

	public SimpleJsonCondition(JsonProperty jsonProperty, ParametrizedValue value, String operator) {
		this.jsonProperty = jsonProperty;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * This method sets ignoreValues boolean to true.
	 * If this boolean is true, value in sql string will be replaced with '?' or with key
	 * from parametrized value in case key is not null.
	 *
	 * @return this
	 */
	public SimpleJsonCondition ignoreValues() {
		this.ignoreValues = true;
		return this;
	}


	public String toSqlString() {
		StringBuilder result = new StringBuilder();
		if (lowerCase) {
			result.append("lower(");
		}
		result.append(jsonProperty.toSqlString());

		if (lowerCase) {
			result.append(")");
		}

		result.append(operator);
		result.append(JsonbConditionUtils.createValue(value, ignoreValues));
		return result.toString();
	}

	@Override
	public List<ParametrizedValue> getParametrizedValues() {
		List<ParametrizedValue> result = new ArrayList<>();
		result.addAll(jsonProperty.getParametrizedValues());
		result.add(this.value);
		return result;
	}

	public SimpleJsonCondition ignoreCase() {
		this.lowerCase = true;
		return this;
	}
}
